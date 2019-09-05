import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentTest {

    public static void main(String[] args) throws IOException {

        String name;
        String surname;
        Scanner in = new Scanner(System.in);

        System.out.println("Enter your name ");
        name = in.next();
        System.out.println("Enter your surname ");
        surname = in.next();
        System.out.println("Dear " + name + " " + surname + ", please answer some questions.");
        
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        QuestionsResourceLoader questionsResourceLoader = (QuestionsResourceLoader) context.getBean("questionResourceLoader");
        Resource questResource = questionsResourceLoader.getResource();

        readCSVFile(questResource);

    }

    private static void readCSVFile(Resource resource) throws IOException {
        String line;
        List<String> questions = new ArrayList<String>();
        List<String> answers = new ArrayList<String>();
        List<String> corAnswer = new ArrayList<String>();

        /*
         * Считывание информации из файла и записывание в списки: вопросов, ответов и правильных ответов
         */

        InputStream input = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while ((line = reader.readLine()) != null) {
            String[] elem = line.split(";");
            for (int i = 0; i < elem.length; i++) {
                if (i == 0) {
                    questions.add(elem[i]);
                } else if (i == 1) {
                    corAnswer.add(elem[i]);
                    answers.add(elem[i]);
                } else {
                    answers.add(elem[i]);
                }
            }
        }
        reader.close();

        printTest(questions, answers, corAnswer);
    }

    /*
     * Печать тестовых заданий
     */

    private static void printTest(List<String> questions, List<String> answers, List<String> corAnswer) {
        String answer;
        Scanner in = new Scanner(System.in);
        int numCorAnswer = 0;
        for (int i = 0; i < 5; i++) {
            System.out.println("Question " + questions.get(i));
            for (int j = (i - 1) * 5 + 5; j < 5 + (i - 1) * 5 + 5; j++) {
                System.out.println(answers.get(j));
            }
            answer = in.next();
            if (answer.trim().equals(corAnswer.get(i)) || answer.trim().equals("1")) {
                numCorAnswer++;
            }
        }
        printMark(numCorAnswer);
    }

    /*
     * Печать количества правильных ответов
     */

    private static void printMark(int number) {
        if (number == 0) {
            System.out.println("There is no right answers. Try again");
        } else if (number == 1) {
            System.out.println("There is 1 right answer.");
        } else if (number == 2) {
            System.out.println("There is 2 right answer.");
        } else if (number == 3) {
            System.out.println("There is 3 right answer.");
        } else if (number == 4) {
            System.out.println("There is 4 right answer.");
        } else if (number == 5) {
            System.out.println("There is 5 right answer. You have good knowledge of subject.");
        }
    }
}

