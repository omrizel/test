package tests;

import org.testng.annotations.Test;

import java.util.HashSet;

public class test123 {

    public void blabla12(String[] arr){
        for (int i=0; i<arr.length; i++){
            for (int j=i+1; j< arr.length; j++){
                System.out.println(arr[i] +","+arr[j]);
            }
        }
    }

    public char test(String str){
        char temp;
        for (int i=0; i < str.length(); i++){
            temp = str.charAt(i);
            for (int j=i+1; j < str.length(); j++){
                if (temp == str.charAt(j)){
                    System.out.println(temp);
                    return temp;
                }
            }


        }
        System.out.println("0");
        return 0;
    }

    // This function prints the first repeated
    // character in str[]
    static char firstRepeating(char str[])
    {
        // Creates an empty hashset
        HashSet<Character> h = new HashSet<>();

        // Traverse the input array from left to right
        for (int i=0; i<=str.length-1; i++)
        {
            char c = str[i];

            // If element is already in hash set, update x
            // and then break
            if (h.contains(c))
                return c;

            else // Else add element to hash set
                h.add(c);
        }

        return '\0';
    }

    public static int findRepeatFirst(String s)
    {
        // this is optimized method
        int p = -1, i, k;

        // initialized counts of occurrences of
        // elements as zero
        int MAX_CHAR = 256;
        int hash[] = new int[MAX_CHAR];

        // initialized positions
        int pos[] = new int[MAX_CHAR];

        for (i = 0; i < s.length(); i++)
        {
            k = (int)s.charAt(i);
            if (hash[k] == 0)
            {
                hash[k]++;
                pos[k] = i;
            }
            else if (hash[k] == 1)
                hash[k]++;
        }

        for (i = 0; i < MAX_CHAR; i++)
        {
            if (hash[i] == 2)
            {
                if (p == -1) // base case
                    p = pos[i];
                else if (p > pos[i])
                    p = pos[i];
            }
        }

        return p;
    }

    @Test
    public void bla(){
        //test("ABC");
      /*  String str = "geeksforgeeks";
        char[] arr = str.toCharArray();
        System.out.println(firstRepeating(arr));*/
        String str = "geeksforgeeks";
        int pos = findRepeatFirst(str);
        if (pos == -1)
            System.out.println("Not found");
        else
            System.out.println(str.charAt(pos));
    }
}
