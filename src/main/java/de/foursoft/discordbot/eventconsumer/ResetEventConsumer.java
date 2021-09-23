package de.foursoft.discordbot.eventconsumer;

import static de.foursoft.discordbot.GuildUtilsAndConstantsAndOtherUsefulStuffAndShit.PASSWORD_CATEGORY_ID;
import static de.foursoft.discordbot.GuildUtilsAndConstantsAndOtherUsefulStuffAndShit.deleteChannel;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class ResetEventConsumer extends GuildMessageReceivedEventConsumer {

	public String getName() {
		return "reset";
	}

	@Override
	public void doExecute(GuildMessageReceivedEvent event) {
		Guild guild = event.getGuild();

		Category category = guild.getCategoryById(PASSWORD_CATEGORY_ID);
		if (category != null) {
			category.getChannels().forEach(passwordChannel -> {

				if (guild.getSelfMember().hasPermission(passwordChannel, Permission.VIEW_CHANNEL)) {
					deleteChannel(passwordChannel);
				}

			});
		}

	}
}
