package ru.otus.vsh.knb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Figure {
    STONE(0, "Камень"), SCISSORS(1, "Ножницы"), PAPER(2, "Бумага"),
    GUN(3, "Ружье"), LIGHTNING(4, "Молния"), DIABLE(5, "Дьявол"),
    DRAGON(6, "Дракон"), WATER(7, "Вода"), AIR(8, "Воздух"),
    SPONGE(9, "Губка"), WOLF(10, "Волк"), TREE(11, "Дерево"),
    MAN(12, "Человек"), SNAKE(13, "Змея"), FIRE(14, "Огонь");

    @Getter
    int order;
    @Getter
    String title;

}
