package common;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {

    public static Map<String, String> parse(String[] args) throws ParseException {
        Option option1 = new Option(Constants.MAX_NUM_THREADS, true, "maximum number of threads to run " +
                "(maxThreads - max 256)");
        option1.setLongOpt(Constants.MAX_NUM_THREADS);

        Option option2 = new Option(Constants.NUM_SKIERS, true,
                "number of skier to generate lift rides for (numSkiers - default 50000)");
        option2.setLongOpt(Constants.NUM_SKIERS);

        Option option3 = new Option(Constants.NUM_LIFT, true, "number of ski lifts (numLifts - range 5-60, " +
                "default 40)");
        option3.setLongOpt(Constants.NUM_LIFT);

        Option option4 = new Option(Constants.DAY_ID, true, "the ski day number - default to 1");
        option4.setLongOpt(Constants.DAY_ID);

        Option option5 = new Option(Constants.RESORT_ID, true, "the resort name which is the resortID - " +
                "default to \"SilverMt\"");
        option5.setLongOpt(Constants.RESORT_ID);

        Option option6 = new Option(Constants.IP_PORT, true, "IP/port address of the server");
        option6.setLongOpt(Constants.IP_PORT);

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        CommandLineParser parser = new DefaultParser();
        CommandLine commands = parser.parse(options, args);

        Map<String, String> map = new HashMap<>();

        if (commands.hasOption(Constants.MAX_NUM_THREADS)) {
            map.put(Constants.MAX_NUM_THREADS, commands.getOptionValue(Constants.MAX_NUM_THREADS));
        } else {
            throw new ParseException("No max number of threads specified.");
        }

        if (commands.hasOption(Constants.NUM_SKIERS)) {
            map.put(Constants.NUM_SKIERS, commands.getOptionValue(Constants.NUM_SKIERS));
        } else {
            map.put(Constants.NUM_SKIERS, Constants.NUM_SKIERS_DEFAULT);
        }

        if (commands.hasOption(Constants.NUM_LIFT)) {
            map.put(Constants.NUM_LIFT, commands.getOptionValue(Constants.NUM_LIFT));
        } else {
            map.put(Constants.NUM_LIFT, Constants.NUM_LIFT_DEFAULT);
        }

        if (commands.hasOption(Constants.DAY_ID)) {
            map.put(Constants.DAY_ID, commands.getOptionValue(Constants.DAY_ID));
        } else {
            map.put(Constants.DAY_ID, Constants.DAY_ID_DEFAULT);
        }

        if (commands.hasOption(Constants.RESORT_ID)) {
            map.put(Constants.RESORT_ID, commands.getOptionValue(Constants.RESORT_ID));
        } else {
            map.put(Constants.RESORT_ID, Constants.RESORT_ID_DEFAULT);
        }

        if (commands.hasOption(Constants.IP_PORT)) {
            map.put(Constants.IP_PORT, commands.getOptionValue(Constants.IP_PORT));
        } else {
            throw new ParseException("No remote ip/port specified.");
        }

        return map;
    }
}
