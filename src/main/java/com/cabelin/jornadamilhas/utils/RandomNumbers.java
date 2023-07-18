package com.cabelin.jornadamilhas.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomNumbers {

  public static Set<Long> generate(long quantity, long max) {
    Random random = new Random();
    Set<Long> randomNumbers = new HashSet<>();

    while (randomNumbers.size() < quantity) {
      long randomNumber = random.nextLong(max);
      randomNumbers.add(randomNumber);
    }

    return randomNumbers;
  }

}
