package LinkedList;
import java.io.*;
import java.util.*;
/*
Input/Output Format For The Custom Input:
Input Format: The first line of input should contain an integer capacity, denoting capacity of LRU cache.
The second line of input should contain an integer n, denoting the number of queries. In next n lines, ith line should contain an integer query_type[i], denoting an entry at index i of query_type array. (i=0,1,...,n-1)
In next line, there should be an integer n. In next n lines, ith line should contain an integer key[i], denoting an entry at index i of key array. (i=0,1,...,n-1)
In next line, there should be an integer n. In next n lines, ith line should contain an integer value[i], denoting an entry at index i of value array. (i=0,1,...,n-1)
If n = 7,
capacity = 2,
index     query_type     key       value
0              1                    5             11
1              1                    10           22
2              0                    5               1
3              1                    15           33
4              0                    10             1
5              1                    5             55
6              0                    5               1
Then input should be:
2
7
1
1
0
1
0
1
0
7
5
10
5
15
10
5
5
7
11
22
1
33
1
55
1

Output Format:
Let’s denote the total no of read queries in input as nr. Then resultant array res returned by solution function will be of size nr.
Hence, there will be nr lines, where ith line contains res[i], denoting entry at index i of res.
For input:
n = 7,
capacity = 2,
index     query_type     key       value
    0              1                    5             11
    1              1                    10           22
    2              0                    5               1
    3              1                    15           33
    4              0                    10             1
    5              1                    5             55
6              0                    5               1
Output will be:
11
-1
55
 */
public class LRUCacheImplementation {


    /*
     * Complete the function below.
     */
    static class Node{
        private int key;
        private int val;
        private Node prev;
        private Node next;

        public Node(int key, int val){
            this.key = key;
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }
    static class LRUCache{
        private Node head;
        private Node tail;
        public LRUCache(){
            this.head = new Node(0,0);
            this.tail = new Node(0,0);
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }

        public void addNode(Node node){
            Node previous = this.tail.prev;
            previous.next = node;
            this.tail.prev = node;
            node.prev = previous;
            node.next = this.tail;


        }
        public void removeNode(Node node){
            Node previous = node.prev;
            previous.next = node.next;
            node.next.prev = previous;
        }
    }
    static int countGetType(int[] k){
        int count = 0;
        for(int key : k){
            if(key == 0){
                count++;
            }
        }
        return count;
    }
    static int[] implement_LRU_cache(int capacity, int[] query_type, int[] key, int[] value) {

        HashMap<Integer, Node> lruMap = new HashMap();
        LRUCache lruCache = new LRUCache();
        int[] ret = new int[countGetType(query_type)];
        int j = 0;
        int k;
        int v;
        Node newNode;

        for(int i = 0; i < query_type.length; i++){
            k = key[i];v = value[i];
            newNode = new Node(k,v);
            if(query_type[i] == 0){
                if(lruMap.containsKey(k)){
                    ret[j] = lruMap.get(k).val;
                    lruCache.removeNode(lruMap.get(k));
                    lruCache.addNode(lruMap.get(k));
                }else{
                    ret[j] = -1;
                }
                j++;
            }else{
                if(lruMap.containsKey(k)){
                    lruCache.removeNode(lruMap.get(k));
                }
                lruCache.addNode(newNode);
                lruMap.put(k, newNode);
                if(lruMap.size() > capacity){
                    lruMap.remove(lruCache.head.next.key);
                    lruCache.removeNode(lruCache.head.next);
                }
            }
        }
        return ret;

    }



    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int[] res;
        int capacity;
        capacity = Integer.parseInt(in.nextLine().trim());

        int query_type_size = 0;
        query_type_size = Integer.parseInt(in.nextLine().trim());

        int[] query_type = new int[query_type_size];
        for(int i = 0; i < query_type_size; i++) {
            int query_type_item;
            query_type_item = Integer.parseInt(in.nextLine().trim());
            query_type[i] = query_type_item;
        }

        int key_size = 0;
        key_size = Integer.parseInt(in.nextLine().trim());

        int[] key = new int[key_size];
        for(int i = 0; i < key_size; i++) {
            int key_item;
            key_item = Integer.parseInt(in.nextLine().trim());
            key[i] = key_item;
        }

        int value_size = 0;
        value_size = Integer.parseInt(in.nextLine().trim());

        int[] value = new int[value_size];
        for(int i = 0; i < value_size; i++) {
            int value_item;
            value_item = Integer.parseInt(in.nextLine().trim());
            value[i] = value_item;
        }

        res = implement_LRU_cache(capacity, query_type, key, value);
        for(int res_i = 0; res_i < res.length; res_i++) {
            bw.write(String.valueOf(res[res_i]));
            bw.newLine();
        }

        bw.close();
    }
}
/*
n actual world key = page number and value = actual page content.



First let's think how we can use single array (or linked list etc.), which will store {key, value} pairs to design LRU cache:



When cache is full we want to remove the least recently used {key, pair}. To keep track of new and old pairs, always we will add new/recently used value at the beginning of the array. Hence {key, value} at the end of the array will be least recently used.



When any set query comes:

Go through the array and search for the key.

1) If key is already present with some value: Remove already present {key, value} from the array. Add {key, new value} at the front of the array.

2) If key is not present: If cache is full then remove one least recently used {key, value} i.e. remove last {key, value} pair. Now add {key, value} at the front of the array.



When any get query comes:

Go through the array and search for the key.

1) If key is already present with some value: Move {key, value} pair to the front of the array (don't forget this!), then return its value.

2) If key is not present: return -1.



This will give correct answer, but time complexity of the set and get will be O(capacity). So time complexity of the code will be O(n * capacity).



Problem with this approach is, in each query we have to traverse the array to find the key. Search can be reduced from O(capacity) to O(1) if we use hash map to store the position of the key in array!



Now we can use linked list with hash map to speed up the process. Linked list to maintain the order of least recently used {key, value} pairs, and hash map for quick search!



Have a look at the optimal_solution1.cpp (solution using built in list) and optimal_solution2.cpp (solution using custom linked list).



Time Complexity:

O(n).

As each get and set query is O(1), and we have total n queries.



Auxiliary Space Used:

O(min(capacity, number of set queries)).

As each set query will increase the size of hash map and list, till we have reached the capacity of the cache.



Space Complexity:

O(n).

Input is O(n) and auxiliary space used is O(min(capacity, number of set queries)).

Now number of set queries <= number of total queries. So number of set queries <= n.

So O(n) + O(min(capacity, number of set queries)) -> O(n) + O(min(capacity, n)) -> O(n).



Slightly slower solution using hash map + max heap is also possible.

In hash map we can store {key, {time stamp, value}} pairs. And in max heap we can store {time stamp, key} pair. {time stamp, key} having largest time stamp will be at the root of the max heap.



We will start with time stamp = 0 and in each query it will be decremented it by 1. (Generally we should increment it, but in C++ built in heap is max heap so we are decrementing time stamp. Suppose one key has time stamp = -5 and other has time stamp = -10, then least recently used key will be the key having larger value (-5) and it should be removed.)



When get query comes:



Check if key is present in hash map or not.



1) If key is not present: return -1.

2) If key is present: Update the time stamp in hash map. Return the value of the key present in hash map. (We are not making any changes in max heap, not even time stamp! Time stamp in hash map will be updated but in max heap it will be the older one. We will do lazy updates when cache is full!)



When set query comes:



Check if key is present in hash map or not.



1) If key is not present: If cache is full then remove least recently used element from hash map and max heap. To find which element to be removed we will use the max heap. We will start looking at the root of the max heap i.e. elements having largest time stamp. We will check if time stamp in max heap and in hash map are same or not. If they are not same then it means the element was accessed, so remove it from top of max heap and insert again with new time stamp that is in hash map. If time stamps are same then that element is least recently used and it should be removed. (Try some examples to understand it more clearly.)



Now add {key, {time stamp, value} in hash map. Also add {time stamp, key} in max heap.



2) If key is present: Update the new value in hash map. Update the time stamp in hash map. (We are not making any changes in max heap, not even time stamp! Time stamp in hash map will be updated but in max heap it will be the older one. We will do lazy updates when cache is full!)





Have a look at other_solution.cpp for solution using hash map + max heap.



Time Complexity:

O(n * log(capacity)).

This solution uses max heap's insert and delete operations, both having time complexity O(log(size of the max heap)), hence time complexity of the solution will become O(n * log(capacity)).



Auxiliary Space Used:

O(min(capacity, number of set queries)).

As each set query will increase the size of hash map and list, till we have reached the capacity of the cache.



Space Complexity:

O(n).

Input is O(n) and auxiliary space used is O(min(capacity, number of set queries)).

Now number of set queries <= number of total queries. So number of set queries <= n.

So O(n) + O(min(capacity, number of set queries)) -> O(n) + O(min(capacity, n)) -> O(n).



Time complexity of solution using (linked list + hash map) is O(n) and time complexity of solution using (max heap + hash map) is O(n * log(capacity)), but when you will run the code, you will not observe the running time difference!



When we ran solutions on some test cases, optimal_solution1.cpp was taking 1.81 seconds, optimal_solution2.cpp was taking 1.80 second and other_solution.cpp was taking 1.81 seconds.



We can see that there is not much difference in optimal_solution1.cpp (built-in linked list) and optimal_solution2.cpp (custom implementation of linked list), but code for optimal_solution2.cpp is complex, so better to use built-in list than implementing own linked list to reduce unnecessary errors.



Some of the reasons together, why other_solution.cpp was not slower are:



1) Caching. This is the most imp thing, affecting the run time. In heap we are using array and hence elements will be stored in continuous memory, but in linked list it will not be the case. Hence other_solution.cpp will better use the caching.

2) Limitations of input size. If we can run code on too many queries (much more than 10^5) with appropriate cache capacity in input, then in worst case optimal_solution1.cpp and optimal_solution2.cpp will be faster (as expected)! (If we can provide too many queries with appropriate cache capacity in input, then we can design input such that other_solution.cpp can not use the caching well, leading optimal_solution1.cpp and optimal_solution2.cpp to be faster.)



There are some limitations ( https://docs.google.com/document/d/1LY31OCgeVpez2k7EcvGO1SyTqcpTljyRoN19n8QK38A/edit?ts=5a4f2404#heading=h.g3c5yh5a2aoo ) of online judges, hence we can given ~ 10^5 queries in one test case. (You will see many times that constraints has something like 1 <= n <= 10^5.) There are some reasons for that 1) Time: Online judges also have limitied resources and hence allow only few seconds for one submission, hence we can not give input that takes minutes to run. 2) Memory: Input having array of size 10^5 will be ~1 MB, now if we give 10^8 in one test case then input file will be ~ 1 GB for one test case! And for one problem we want to test your solution over multiple test cases (to take care of all corner cases and worst cases). Hence generating input files in GBs and uploading it on online judge is not feasible. Also many languages like python are slow to read files, hence it will take more time to read input!



We suggest you to implement both type of solutions and experiment with different inputs.



For other_solution.cpp worst case example with n = 15:



capacity: 7

n = 15

query_type = [1 1 1 1 1 1 1 0 0 0 0 0 0 0 1]

key = [1 2 3 4 5 6 7 1 2 3 4 5 6 7 8]

val = [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15]



First we will add 7 key with values. After this cache will be full.

Then we will access all of them hence time stamp will be updated for all keys.

Now we will add one new key. We have accessed all 7 keys, hence in set function call for {8 -> 15}, while lopp will run 7 times (all previous keys), with each taking log(capacity) = log(7) time to run. Hence we will get overall O(n * log(k)) time complexity.



In optimal_solution1.cpp and optimal_solution2.cpp each query will be O(1), but constant hidden is large due to linked list, but with variable change it will not change more, but here with variable change log(k) can change, hence with large number of queries and appropriate cache capacity in input, other_solution.cpp can be made slower than optimal_solution1.cpp and optimal_solution2.cpp. (But it is not possible to provide such large input on online judges, so you will never encounter that other_solution.cpp is much slower!)



 */
