package com.gunjan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;


public class Person implements Cloneable
{
    static List<Person> roster = new ArrayList<Person>()
    {{
        add(new Person("bob", LocalDate.of(1986, 9, 10), Sex.MALE, "bob@gmail.com", 25));
        add(new Person("alice", LocalDate.of(1985, 9, 10), Sex.FEMALE, "alice@gmail.com", 27));
        add(new Person("john", LocalDate.of(1984, 9, 10), Sex.MALE, "john.bit2k41@gmail.com", 27));
    }};
    
    private String name;
    private LocalDate birthday;
    private Sex gender;
    private String emailAddress;
    private int age;
    
    public Person(String name, LocalDate birthday, Sex gender, String emailAddress, int age)
    {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.age = age;
    }
    
    public Person()
    {
    
    }
    
    public enum Sex
    {
        MALE,
        FEMALE
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public LocalDate getBirthday()
    {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday)
    {
        this.birthday = birthday;
    }
    
    public Sex getGender()
    {
        return gender;
    }
    
    public void setGender(Sex gender)
    {
        this.gender = gender;
    }
    
    public String getEmailAddress()
    {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    @Override
    public String toString()
    {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", emailAddress='" + emailAddress + '\'' +
                ", age=" + age +
                '}';
    }
    
    public static int compareByAge(Person a, Person b)
    {
        return a.birthday.compareTo(b.birthday);
    }
    
    public static void printPersons(List<Person> roster, CheckPerson tester)
    {
        for(Person p : roster)
        {
            if(tester.test(p))
            {
                System.out.println(p.toString());
            }
        }
    }
    
    public static void printPersonsWithPredicate(List<Person> roster, Predicate<Person> tester)
    {
        for(Person p : roster)
        {
            if(tester.test(p))
            {
                System.out.println(p.toString());
            }
        }
    }
    
    public static void printPersonsWithPredicateAndConsumer(List<Person> roster, Predicate<Person> tester, Consumer<Person> block)
    {
        for(Person p : roster)
        {
            if(tester.test(p))
            {
                block.accept(p);
            }
        }
    }
    
    public static void printPersonsWithPredicateAndFunctionAndConsumer(
            List<Person> roster,
            Predicate<Person> tester,
            Function<Person,String> mapper, Consumer<String> block
    )
    {
        for(Person p : roster)
        {
            if(tester.test(p))
            {
                String data = mapper.apply(p);
                block.accept(data);
            }
        }
    }
    
    public static <X, Y> void processElements(
            Iterable<X> source,
            Predicate<X> tester,
            Function<X,Y> mapper,
            Consumer<Y> block)
    {
        for(X p : source)
        {
            if(tester.test(p))
            {
                Y data = mapper.apply(p);
                block.accept(data);
            }
        }
    }
    
    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>> DEST transferElements(SOURCE sourceCollection, Supplier<DEST> collectionFactory)
    {
        DEST result = collectionFactory.get();
        for(T t : sourceCollection)
        {
            result.add(t);
        }
        return result;
    }
    
    @Test
    public void test1()
    {
        printPersons(roster, (Person p) -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
    }
    
    @Test
    public void test2()
    {
        printPersons(
                roster,
                new CheckPerson()
                {
                    public boolean test(Person p)
                    {
                        return p.getGender() == Sex.MALE
                                && p.getAge() >= 18
                                && p.getAge() <= 25;
                    }
                }
        );
        
        
    }
    
    @Test
    public void test3()
    {
        printPersons(roster, (Person p) -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
        
    }
    
    @Test
    public void test4()
    {
        printPersonsWithPredicate(roster, (Person p) -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
    }
    
    
    @Test
    public void test5()
    {
        printPersonsWithPredicateAndConsumer(roster, (Person p) -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25
                ,
                person -> System.out.println(person)
        );
        
        
    }
    
    @Test
    public void test6()
    {
        printPersonsWithPredicateAndFunctionAndConsumer(roster, (Person p) -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 28
                ,
                person -> person.getEmailAddress(), email -> System.out.println(email)
        );
    }
    
    @Test
    public void test7()
    {
        processElements(
                roster,
                p -> p.getGender() == Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 28,
                p -> p.getEmailAddress(),
                email -> System.out.println(email)
        );
    }
    
    @Test
    public void test8()
    {
        roster.stream().filter(p -> p.getGender() == Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25)
                .map(p -> p.getEmailAddress()).forEach(email -> System.out.println(email));
    }
    
    @Test
    public void test9()
    {
        roster.stream().forEach(person -> System.out.println(person));
    }
    
    @Test
    public void test10()
    {
        roster.stream().map(person -> person.getEmailAddress()).forEach(email -> System.out.println(email));
    }
    
    @Test
    public void test11()
    {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
        
        Arrays.sort(rosterAsArray, new PersonAgeComparator());
        for(Person person : rosterAsArray)
        {
            System.out.println(person);
        }
    }
    
    @Test
    public void test12()
    {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
        Arrays.sort(rosterAsArray, (p1, p2) -> p2.getBirthday().compareTo(p1.getBirthday()));
        for(Person person : rosterAsArray)
        {
            System.out.println(person);
        }
    }
    
    @Test
    public void test13()
    {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
        Arrays.sort(rosterAsArray, (p1, p2) -> Person.compareByAge(p1, p2));
        for(Person person : rosterAsArray)
        {
            System.out.println(person);
        }
    }
    
    @Test
    public void test14()
    {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
        
        BiFunction<Person,Person,Integer> fun = Person::compareByAge;
        Arrays.sort(rosterAsArray, (t, u) -> fun.apply(t, u));
        
        Arrays.sort(rosterAsArray, Person::compareByAge);
        
        for(Person person : rosterAsArray)
        {
            System.out.println(person);
        }
    }
    
    @Test
    public void test15()
    {
        Person[] rosterAsArray = roster.toArray(new Person[roster.size()]);
        ComparisonProvider myComparisonProvider = new ComparisonProvider();
        Arrays.sort(rosterAsArray, myComparisonProvider::compareByName);
    }
    
    @Test
    public void test16()
    {
        String[] stringArray = {"Barbara", "James", "Mary", "John", "Patricia", "Robert", "Michael", "Linda"};
        Arrays.sort(stringArray, String::compareToIgnoreCase);
    }
    
    @Test
    public void test17()
    {
        String[] stringArray = {"Barbara", "James", "Mary", "John", "Patricia", "Robert", "Michael", "Linda"};
        Arrays.sort(stringArray, (s1, s2) -> s1.compareToIgnoreCase(s2));
    }
    
    @Test
    public void test18()
    {
        Set<Person> rosterSet1 = transferElements(roster, () -> {
            return new HashSet<>();
        });
        System.out.println(rosterSet1);
    }
    
    @Test
    public void test19()
    {
        Set<Person> rosterSet2 = transferElements(roster, HashSet::new);
        System.out.println(rosterSet2);
    }
    
    @Test
    public void test20()
    {
        Set<Person> rosterSet3 = transferElements(roster, HashSet<Person>::new);
        System.out.println(rosterSet3);
    }
    
    @Test
    public void test21()
    {
        double avarageAge = roster.stream().filter(p -> p.getGender().equals(Sex.MALE)).mapToDouble(Person::getAge).average().getAsDouble();
    }
    
    @Test
    public void test22()
    {
        Integer totalAge = roster.stream().mapToInt(Person::getAge).sum();
        System.out.println(totalAge);
    }
    
    @Test
    public void test23()
    {
        Integer totalAgeReduce = roster
                .stream()
                .map(Person::getAge)
                .reduce(
                        0,
                        (a, b) -> a + b);
        
        System.out.println(totalAgeReduce);
    }
    
    @Test
    public void test24()
    {
        Average averageCollect = roster.stream()
                .filter(p -> p.getGender() == Sex.MALE)
                .map(Person::getAge)
                .collect(Average::new, Average::accept, Average::combine);
        
        System.out.println("Average age of male members: " +
                averageCollect.average());
    }
    
    @Test
    public void test25()
    {
        List<Integer> ages = roster.stream()
                .filter(p -> p.getGender() == Sex.MALE)
                .map(Person::getAge)
                .collect(Collectors.toList());
    }
    
    @Test
    public void test26()
    {
        List<Integer> ages1 = roster.stream()
                .filter(p -> p.getGender() == Sex.MALE)
                .map(Person::getAge)
                .collect(ArrayList<Integer>::new, ArrayList::add, (left, right) -> {
                    left.addAll(right);
                });
        System.out.println(ages1);
    }
    
    @Test
    public void test27()
    {
        Map<Sex,List<Person>> byGender =
                roster
                        .stream()
                        .collect(
                                Collectors.groupingBy(Person::getGender));
    }
    
    @Test
    public void test28()
    {
        Map<String,Person> agesMap = roster.stream()
                .filter(p -> p.getGender() == Sex.MALE)
                .collect(Collectors.toMap(Person::getName, person -> person));
    }
    
    @Test
    public void test29()
    {
        Map<Sex,List<String>> namesByGender =
                roster.stream().collect(
                        Collectors.groupingBy(
                                Person::getGender,
                                Collectors.mapping(
                                        Person::getName,
                                        Collectors.toList())));
        
    }
    
    @Test
    public void test30()
    {
        List<Integer> ages = roster.stream().collect(new AgeCollector());
        System.out.println(ages);
        
    }
    
    @Test
    public void test31()
    {
        ImmutableSet<Person> ages = roster.stream().collect(new ImmutableSetCollector<Person>());
        System.out.println(ages);
    }
    
    @Test
    public void test32()
    {
        Map<Sex,List<String>> ages = roster.stream().collect(new NamesByGenderCollector());
        System.out.println(ages);
    }
    
    @Test
    public void test33()
    {
        BinaryOperator<Integer> adder = (n1, n2) -> n1 + n2;
        
        System.out.println(adder.apply(3, 4));
    }
    
    @Test
    public void test34()
    {
        boolean exist = roster.stream().collect(Collectors.collectingAndThen(new AgeCollector(), p -> p.contains(25)));
        System.out.println(exist);
    }
    
    @Test
    public void test35()
    {
        Map<Integer,Set<String>> map = roster.stream().collect(Collectors.groupingBy(Person::getAge, Collector.of(HashSet::new, (set, p) -> set.add(p.getName()), (l, r) -> {
            l.addAll(r);
            return l;
        })));
        System.out.println(map);
    }
    
    
    @Test
    public void test36() throws CloneNotSupportedException
    {
        
        int totalSize = 8000000;
        Person person = new Person("bob", LocalDate.of(1986, 9, 10), Sex.MALE, "bob@gmail.com", 25);
        
        long start0 = System.currentTimeMillis();
        for(int j = 0; j < totalSize; j++)
        {
            Person personClone = (Person)person.clone();
            personClone.setAge(j);
            personClone.setName("Bob" + j);
            personClone.setEmailAddress("bob@gmail.com" + j);
            roster.add(personClone);
        }
        long end0 = System.currentTimeMillis();
        
        long millis = end0 - start0;
        
        System.out.println("Time taken to create " + totalSize + " Person objects is " + getDurationBreakdown(millis));
        /*long start1 = System.currentTimeMillis();
        roster.parallelStream().filter(p -> {boolean status = p.getName().contains("Bob");
        for(int i = 0 ; i < 100000 ; i++){
            new String("==============");
        }
            return status;}).collect(new ListCollector());
        long end1 = System.currentTimeMillis();
        millis = end1 - start1;
        System.out.println("Time took to process using parallel stream of size " + totalSize + " is " + getDurationBreakdown(millis));

        long start2 = System.currentTimeMillis();
        roster.stream().filter(p -> {boolean status = p.getName().contains("Bob");
            for(int i = 0 ; i < 100000 ; i++){
                new String("==============");
            }
            return status;}).collect(new ListCollector());
        long end2 = System.currentTimeMillis();
        millis = end2 - start2;
        System.out.println("Time took to process using stream of size " + totalSize + " is " + getDurationBreakdown(millis));*/
    }
    
    @Test
    public void test37()
    {
        Function<Computer,Integer> getAge = Computer::getAge;
        Function<Integer,Computer> function = Computer::new;
        BiFunction<Integer,Integer,Computer> biFunction = Computer::new;
        TriFunction<Integer,Integer,String,Computer> triFunction = Computer::new;
        System.out.println(getAge.apply(function.apply(10)));
        System.out.println(biFunction.apply(10, 100));
        System.out.println(triFunction.apply(10, 100, "Lenovo"));
        
        Function<Integer,Computer[]> computerCreator = Computer[]::new;
        Computer[] computerArray = computerCreator.apply(5);
        System.out.println(computerArray);
    }
    
    @FunctionalInterface
    interface TriFunction<A, B, C, R>
    {
        R apply(A a, B b, C c);
        
        /*default <V> TriFunction<A,B,C,V> andThen(Function<? super R,? extends V> after)
        {
            Objects.requireNonNull(after);
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }*/
    }
    
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis)%24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis)%60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)%60;
        long milliseconds = millis%1000;
        
        return String.format("%d:%d:%d", minutes, seconds, milliseconds);
    }
}

class Computer
{
    int age = 10;
    int model = 1010;
    String brand = "Dell";
    
    public Computer()
    {
    
    }
    
    public Computer(int age)
    {
        this.age = age;
    }
    
    public Computer(int age, int model)
    {
        this.age = age;
        this.model = model;
    }
    
    public Computer(int age, int model, String brand)
    {
        this.age = age;
        this.model = model;
        this.brand = brand;
    }
    
    Integer getAge()
    {
        return this.age;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Computer{");
        sb.append("age=").append(age);
        sb.append(", model=").append(model);
        sb.append(", brand='").append(brand).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class ListCollector implements Collector<Person,List<Person>,List<Person>>
{
    
    @Override
    public Supplier<List<Person>> supplier()
    {
        return ArrayList<Person>::new;
    }
    
    @Override
    public BiConsumer<List<Person>,Person> accumulator()
    {
        return (a, p) -> a.add(p);
    }
    
    @Override
    public BinaryOperator<List<Person>> combiner()
    {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }
    
    @Override
    public Function<List<Person>,List<Person>> finisher()
    {
        return p -> p;
    }
    
    @Override
    public Set<Characteristics> characteristics()
    {
        return EnumSet.of(Characteristics.IDENTITY_FINISH);
    }
}

class Average implements IntConsumer
{
    private int total = 0;
    private int count = 0;
    
    
    public double average()
    {
        return count > 0 ? ((double)total)/count : 0;
    }
    
    @Override
    public void accept(int i)
    {
        total += i;
        count++;
    }
    
    public void combine(Average other)
    {
        total += other.total;
        count += other.count;
    }
}

class Calculator
{
    
    interface IntegerMath
    {
        int operation(int a, int b);
    }
    
    public int operateBinary(int a, int b, IntegerMath op)
    {
        return op.operation(a, b);
    }
    
    
    public static void main(String... args)
    {
        
        Calculator myApp = new Calculator();
        IntegerMath additionOld = new IntegerMath()
        {
            @Override
            public int operation(int a, int b)
            {
                return a + b;
            }
        };
        IntegerMath addition = (a, b) -> a + b;
        
        IntegerMath addition1 = (a, b) -> {
            // Multiple line method implementation
            return a + b;
        };
        
        IntegerMath subtractionOld = new IntegerMath()
        {
            @Override
            public int operation(int a, int b)
            {
                return a - b;
            }
        };
        
        IntegerMath subtraction = (a, b) -> a - b;
        System.out.println("40 + 2 = " +
                myApp.operateBinary(40, 2, addition));
        System.out.println("20 - 10 = " +
                myApp.operateBinary(20, 10, subtraction));
    }
}

class ComparisonProvider
{
    public int compareByName(Person a, Person b)
    {
        return a.getName().compareTo(b.getName());
    }
    
    public int compareByAge(Person a, Person b)
    {
        return a.getBirthday().compareTo(b.getBirthday());
    }
}

interface CheckPerson
{
    boolean test(Person p);
}

class PersonAgeComparator implements Comparator<Person>
{
    public int compare(Person a, Person b)
    {
        return a.getBirthday().compareTo(b.getBirthday())*-1;
    }
}

class AgeCollector implements Collector<Person,List<Integer>,List<Integer>>
{
    
    
    @Override
    public Supplier<List<Integer>> supplier()
    {
        return ArrayList<Integer>::new;
    }
    
    @Override
    public BiConsumer<List<Integer>,Person> accumulator()
    {
        return (a, p) -> a.add(p.getAge());
    }
    
    @Override
    public BinaryOperator<List<Integer>> combiner()
    {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }
    
    @Override
    public Function<List<Integer>,List<Integer>> finisher()
    {
        return p -> p;
    }
    
    @Override
    public Set<Characteristics> characteristics()
    {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}

class ImmutableSetCollector<T>
        implements Collector<T,ImmutableSet.Builder<T>,ImmutableSet<T>>
{
    @Override
    public Supplier<ImmutableSet.Builder<T>> supplier()
    {
        return ImmutableSet::builder;
    }
    
    @Override
    public BiConsumer<ImmutableSet.Builder<T>,T> accumulator()
    {
        return (builder, t) -> builder.add(t);
    }
    
    @Override
    public BinaryOperator<ImmutableSet.Builder<T>> combiner()
    {
        return (left, right) -> {
            left.addAll(right.build());
            return left;
        };
    }
    
    @Override
    public Function<ImmutableSet.Builder<T>,ImmutableSet<T>> finisher()
    {
        return ImmutableSet.Builder::build;
    }
    
    @Override
    public Set<Characteristics> characteristics()
    {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}

class NamesByGenderCollector implements Collector<Person,Map<Person.Sex,List<String>>,Map<Person.Sex,List<String>>>
{
    
    @Override
    public Supplier<Map<Person.Sex,List<String>>> supplier()
    {
        return HashMap<Person.Sex,List<String>>::new;
    }
    
    @Override
    public BiConsumer<Map<Person.Sex,List<String>>,Person> accumulator()
    {
        return
                (a, p) ->
                {
                    List<String> list = a.get(p.getGender());
                    if(list == null)
                    {
                        list = new ArrayList<>();
                        a.put(p.getGender(), list);
                    }
                    list.add(p.getName());
                };
    }
    
    @Override
    public BinaryOperator<Map<Person.Sex,List<String>>> combiner()
    {
        return (l, r) -> {
            l.putAll(r);
            return l;
        };
    }
    
    @Override
    public Function<Map<Person.Sex,List<String>>,Map<Person.Sex,List<String>>> finisher()
    {
        return f -> f;
    }
    
    @Override
    public Set<Characteristics> characteristics()
    {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}