import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Class<?> targetClass = DynamicMethod.class;
        Method[] methods = targetClass.getDeclaredMethods();

        Arrays.sort(methods, (m1, m2) -> {
            MenuOption menuOption1 = m1.getAnnotation(MenuOption.class);
            MenuOption menuOption2 = m2.getAnnotation(MenuOption.class);
            
            if (menuOption1 != null && menuOption2 != null) {
                return Integer.compare(menuOption1.id(), menuOption2.id());
            }
            return 0;
        });

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