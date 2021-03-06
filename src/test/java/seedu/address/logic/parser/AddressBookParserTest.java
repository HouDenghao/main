package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.PORTRAIT_DESC_FIRST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PORTRAIT_PATH_FIRST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DisjoinCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditEventCommand;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindEventCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PortraitCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ShowParticipantsCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortEventCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventNameContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PortraitPath;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.EventDescriptorBuilder;
import seedu.address.testutil.EventsUtil;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    //@@author Adoby7
    @Test
    public void parseCommandAddEvent() throws Exception {
        Event event = new EventBuilder().build();
        AddEventCommand command = (AddEventCommand) parser.parseCommand(AddEventCommand.COMMAND_WORD + " "
                + EventsUtil.getEventDetails(event));
        assertEquals(new AddEventCommand(event), command);
    }

    @Test
    public void parseCommandDisjoin() throws Exception {
        Index personIndex = INDEX_SECOND_PERSON;
        Index eventIndex = INDEX_FIRST_EVENT;
        DisjoinCommand command = (DisjoinCommand) parser.parseCommand(DisjoinCommand.COMMAND_WORD + " "
                + PREFIX_EVENT + eventIndex.getOneBased() + " " + PREFIX_PERSON + personIndex.getOneBased());
        assertEquals(new DisjoinCommand(personIndex, eventIndex), command);
    }

    @Test
    public void parseCommandPortrait() throws Exception {
        PortraitCommand command = (PortraitCommand) parser.parseCommand(PortraitCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PORTRAIT_DESC_FIRST);
        assertEquals(new PortraitCommand(INDEX_FIRST_PERSON, new PortraitPath(VALID_PORTRAIT_PATH_FIRST)), command);
    }

    @Test
    public void parseCommandEditEvent() throws Exception {
        Event event = new EventBuilder().build();
        EditEventDescriptor descriptor = new EventDescriptorBuilder(event).build();
        EditEventCommand command = (EditEventCommand) parser.parseCommand(EditEventCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + EventsUtil.getEventDetails(event));
        assertEquals(new EditEventCommand(INDEX_FIRST_PERSON, descriptor), command);
    }
    //@@author

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    // @@author HouDenghao
    @Test
    public void parseCommandFindE() throws Exception {
        List<String> keywords = Arrays.asList("first", "second", "third");
        FindEventCommand command = (FindEventCommand) parser.parseCommand(
                FindEventCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindEventCommand(new EventNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommandShowP() throws Exception {
        ShowParticipantsCommand command = (ShowParticipantsCommand) parser.parseCommand(
                ShowParticipantsCommand.COMMAND_WORD + " " + INDEX_FIRST_EVENT.getOneBased());
        assertEquals(new ShowParticipantsCommand(INDEX_FIRST_EVENT), command);
    }

    @Test
    public void parseCommandSort() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " 3") instanceof SortCommand);
    }

    @Test
    public void parseCommandSortE() throws Exception {
        assertTrue(parser.parseCommand(SortEventCommand.COMMAND_WORD) instanceof SortEventCommand);
        assertTrue(parser.parseCommand(SortEventCommand.COMMAND_WORD + " 3") instanceof SortEventCommand);
    }

    // @@author
    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
