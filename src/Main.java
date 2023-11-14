import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        Class<?> targetClass = DynamicMethod.class;
        Method[] methods = targetClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MenuOption.class)) {
                MenuOption menuOption = method.getAnnotation(MenuOption.class);
                if (menuOption != null) {
                    System.out.println(menuOption.name());
                }
                
            }
        }

        int option = 1;
        int i = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(MenuOption.class)) {
                if (i == option) {

                    try {
                        method.invoke(targetClass.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                }

                i++;
            }
        }

    }
}