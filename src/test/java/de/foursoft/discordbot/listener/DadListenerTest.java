package de.foursoft.discordbot.listener;


import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import net.dv8tion.jda.api.entities.TextChannel;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class DadListenerTest {

	@Mock
	private TextChannel channel;

	@Mock
	private GuildMessageReceivedEvent guildMessageReceivedEvent;

	@Test
	void should_return_correct_message_in_channel() {
		// GIVEN
//		when(guildMessageReceivedEvent.getMessage()).thenReturn(n);


		// WHEN


		// THEN
	}
}