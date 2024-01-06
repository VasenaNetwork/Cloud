package com.bedrockcloud.bedrockcloud.utils.console.reader;

import java.util.*;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.Command;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

public class ConsoleReader extends Thread implements Loggable {
    private final Set<Command> commands;
    private String message;

    public ConsoleReader() {
        this.setName("ConsoleReader-main");
        this.commands = new HashSet<Command>();
    }

    @Override
    public void run() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .nativeSignals(true)
                    .signalHandler(Terminal.SignalHandler.SIG_DFL)
                    .build();
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(new DefaultParser())
                    .completer(new CommandCompleter(getCommandNames()))
                    .build();

            while (BedrockCloud.isRunning()) {
                String input = lineReader.readLine();

                ParsedLine pl = lineReader.getParsedLine();
                String commandPart = pl.words().get(0);

                final String[] args = this.dropFirstString(input.split(" "));
                executeCommand(commandPart, args);

                lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                lineReader.getTerminal().puts(InfoCmp.Capability.delete_line);
                lineReader.getTerminal().writer().flush();
            }

            terminal.close();
        } catch (Exception e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    public String[] dropFirstString(final String[] input) {
        final String[] result = new String[input.length - 1];
        System.arraycopy(input, 1, result, 0, input.length - 1);
        return result;
    }

    public Set<String> getCommandNames() {
        Set<String> commandNames = new HashSet<>();
        for (Command command : this.commands) {
            commandNames.add(command.getCommand());
        }
        return commandNames;
    }

    public void addCommand(final Command command) {
        this.commands.add(command);
    }

    public void executeCommand(final String commandName, final String[] args) {
        final Command command = this.getCommand(commandName);
        if (command != null) {
            command.onCommand(args);
        } else if (!commandName.isEmpty() && !commandName.isBlank()) {
            BedrockCloud.getLogger().command("This Command doesn't exist. Try help for more!");
        }
    }

    public Command getCommand(final String commandName) {
        for (final Command command : this.commands) {
            if (command.getCommand().equalsIgnoreCase(commandName)) {
                return command;
            }
        }
        return null;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    private class CommandCompleter implements Completer {
        private final Set<String> commandNames;

        CommandCompleter(Set<String> commandNames) {
            this.commandNames = commandNames;
        }

        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String word = line.word();
            if (line.words().size() == 1) {
                for (String cmd : commandNames) {
                    if (cmd.startsWith(word)) {
                        candidates.add(new Candidate(cmd));
                    }
                }
            }
        }
    }
}