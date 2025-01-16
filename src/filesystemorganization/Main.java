package filesystemorganization;

import filesystemorganization.command.CommandHandler;
import filesystemorganization.model.FileSystemOrganization;

/**
 * This class is the entry point of the program.
 *
 * @author Programmieren-Team
 * @author ujxbs
 */
public final class Main {
    private static final String GREETING_MESSAGE = "Use one of the following commands: load <path>, run <id>, "
            + "change <id> <file> <number>, quit";
    private static final String UTILITY_CLASS_CONSTRUCTOR_MESSAGE = "Utility classes cannot be instantiated";

    private Main() {
        throw new UnsupportedOperationException(UTILITY_CLASS_CONSTRUCTOR_MESSAGE);
    }
    
    /**
     * Starts the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileSystemOrganization fileSystemOrganization = new FileSystemOrganization();
        System.out.println(GREETING_MESSAGE);
        CommandHandler commandHandler = new CommandHandler(fileSystemOrganization);
        commandHandler.handleUserInput();
    }
}
