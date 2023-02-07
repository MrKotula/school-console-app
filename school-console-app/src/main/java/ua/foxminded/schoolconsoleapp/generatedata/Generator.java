package ua.foxminded.schoolconsoleapp.generatedata;

import java.util.List;

public interface Generator<T> {
    List<T> generate(int numbersOf);
}
