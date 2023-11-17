import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DynamicMenu {

    @MenuOption(name = "Option 1", id = 1)
    public void method1() {
        System.out.println("DynamicMethod.method1()");
    }

    @MenuOption(name = "Option 2", id = 2)
    public void method2() {
        System.out.println("DynamicMethod.method2()");
    }

    @MenuOption(name = "Option 3", id = 3)
    public void method3() {
        System.out.println("DynamicMethod.method3()");
    }

    @MenuOption(name = "Exit", id = 4)
    public void method4() {
        System.out.println("Exiting");
    }

    public Method[] getOptionMethods() {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MenuOption.class)) {
                methods.add(method);
            }
        }

        if (methods.size() == 0) {
            throw new RuntimeException("There are no annotated methods");
        }

        return sortMethods(methods.toArray(new Method[0]));
    }

    public Method getExitOptionMethod () {
        Method[] methods = getOptionMethods();

        for (Method method : methods) {
            MenuOption menuOption = method.getAnnotation(MenuOption.class);
            if (menuOption != null && menuOption.name().equalsIgnoreCase("Exit")) {
                return method;
            }
        }

        return null;
    }

    public int getExitOptionMethodId() {
        Method exitMethod = getExitOptionMethod();
        if (exitMethod != null) {
            MenuOption menuOption = exitMethod.getAnnotation(MenuOption.class);
            if (menuOption != null) {
                return menuOption.id();
            }
            else {
                throw new RuntimeException("An error occurred: method has no annotation");
            }
        }
        throw new RuntimeException("An error occurred: no exit method found");
    }

    public Method[] sortMethods(Method[] methods) {
        Arrays.sort(methods, (m1, m2) -> {
            MenuOption menuOption1 = m1.getAnnotation(MenuOption.class);
            MenuOption menuOption2 = m2.getAnnotation(MenuOption.class);

            if (menuOption1 != null && menuOption2 != null) {
                return Integer.compare(menuOption1.id(), menuOption2.id());
            }
            return 0;
        });
        return methods;
    }

    public void menu() {

        int exitOptionMethod;
        try {
            exitOptionMethod = getExitOptionMethodId();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }


        int menuOption; 
        do {
            System.out.println("\nMENU\n");
            printMethods();
            System.out.print("\nInput option (1-" + getOptionMethods().length + "): ");
            try {
                menuOption = inputMenuOption();
                System.out.println();
                executeMethod(menuOption - 1);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return;
            }
        } while (menuOption != exitOptionMethod);

    }

    public void printMethods() {
        Method[] methods = getOptionMethods();
        for (Method method : methods) {
            MenuOption menuOption = method.getAnnotation(MenuOption.class);
            if (menuOption != null) {      //ensure that the method has the annotation
                System.out.println(menuOption.id() + " - " + menuOption.name());
            }
        }
    }

    public int inputMenuOption () {
        Scanner scanner = new Scanner (System.in);

        int methodsLength = getOptionMethods().length;

        if (methodsLength == 0) {
            throw new RuntimeException("There are no annotated methods");
        }
        int menuOption;
        do {
            menuOption = scanner.nextInt();
            if (menuOption < 0 || menuOption > methodsLength) {
                System.out.print("\nPlease enter an option between 1 and " + methodsLength + " : ");
            }
        } while (menuOption < 0 || menuOption > methodsLength);

        return menuOption;
    }

    public void executeMethod(int option) throws Exception{
        Method[] methods = getOptionMethods();
        int i = 0;

        for (Method method : methods) {
            if (i == option) {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    throw e;
                }
            }
            i++;
        }
    }
}
