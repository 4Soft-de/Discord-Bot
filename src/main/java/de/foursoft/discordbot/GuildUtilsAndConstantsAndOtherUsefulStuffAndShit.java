package de.foursoft.discordbot;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

public final class GuildUtilsAndConstantsAndOtherUsefulStuffAndShit {

	private GuildUtilsAndConstantsAndOtherUsefulStuffAndShit() {
		//HIDDEN
	}

	public static final long PASSWORD_CATEGORY_ID = 852893820983050240L;

	public static void deleteChannel(GuildChannel passwordChannel) {
		deleteChannel(passwordChannel, 0, TimeUnit.MILLISECONDS);
	}

	public static void deleteChannel(GuildChannel passwordChannel, long timeoutValue, TimeUnit timeUnit) {
		passwordChannel.delete().queueAfter(timeoutValue, timeUnit, null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_CHANNEL));
	}

}
