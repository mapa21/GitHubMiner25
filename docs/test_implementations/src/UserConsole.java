//    public static String getInput(String prompt, Set<String> options) {
//        String line;
//
//        try (ConsoleReader console = new ConsoleReader()) {
////            console.setHistoryEnabled(true);
//
//            console.addCompleter(new CaseInsensitiveCompleter(options));
//
//            console.setPrompt(prompt + "> ");
//            while (!options.contains(line = console.readLine().toLowerCase().trim())) {
//                //TODO: print options
//                console.println("Invalid");
//            }
//
//        }
//        catch (java.io.IOException e) {
//            line = null;
//            //TODO: handle error
//        }
//        return line;
//    }