/*
WebShooter Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.wumple.webslinger.webbing;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerInWebMessage implements IMessage {
		
	// The coordinates of the web block
	public BlockPos pos;

	public PlayerInWebMessage() {}

	public PlayerInWebMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class Handler implements IMessageHandler<PlayerInWebMessage, IMessage>{
		@Override
		public IMessage onMessage(final PlayerInWebMessage msg, MessageContext ctx) {
			final Minecraft mc = Minecraft.getMinecraft();
			mc.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					mc.world.setBlockState(msg.pos, Blocks.WEB.getDefaultState());
					mc.player.setInWeb();
				}
			});
			return null;
		}
	}
	
	public static void register(SimpleNetworkWrapper netWrapper) {
		netWrapper.registerMessage(PlayerInWebMessage.Handler.class, PlayerInWebMessage.class, 0, Side.CLIENT);
	}

}
