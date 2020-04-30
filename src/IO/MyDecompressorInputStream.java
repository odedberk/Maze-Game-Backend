package IO;

import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }
    public int read(byte[] byteArray)throws IOException{
        //byte []newByteArray = new byte[12];
        //byte []compressedMaze = new byte[1000000000];
        //in.read(compressedMaze);
        ArrayList<Integer> array = new ArrayList<>();
        int next = in.read();
        while (next!=-1) {
            array.add(next);
            next=in.read();
        }

        // LinkedList<Integer> unCompressMaze= new LinkedList<>();
        for(int i=0; i<12 ; i++) {
            int temp = array.get(i);
            byteArray[i] =(byte)temp;
        }
        //unCompressMaze.add(array.get(i));
        //int sizeOfPair =in.read();
        LinkedList<Pair<Integer,Integer>> dictionary = getDictionary(array,array.get(12));
//        HashSet<Pair<Integer,String>> set = new HashSet<>();
        ArrayList<String> deCompress = new ArrayList<>();
        deCompress.add("");
        for(int i=1; i<dictionary.size(); i++){
            //Stack<Integer> temp = new Stack<>();
            String temp = "";
            if(i != dictionary.size()-1)
                temp="" +dictionary.get(i).getKey();
            else{
                if(dictionary.get(i).getKey()!=2)
                    temp=""+dictionary.get(i).getKey();
            }
            //temp.add(dictionary.get(i).getKey());
            if(dictionary.get(i).getValue()>=1)
                temp=deCompress.get(dictionary.get(i).getValue())+temp;
            deCompress.add(i,temp);
            // int p = dictionary.get(i).getValue();
            // while (p>=1){
            //   temp.add(dictionary.get(p).getKey());
            // p=dictionary.get(p).getValue();
            //}
//            while (!temp.isEmpty()) {
//                if (temp.peek() == 2)
//                    temp.pop();
//                else
//                    unCompressMaze.add(temp.pop());
//            }
        }
        //byte []newByteMaze = new byte[byteArr.size()];
//        for(int i=0; i<byteArray.length && i<unCompressMaze.size(); i++){
//            int temp =unCompressMaze.get(i);
//            byteArray[i]=(byte)temp;
//        }
        int insert=12;
        for (String temp : deCompress){
            for(char c : temp.toCharArray()) {
                if(c=='1')
                    byteArray[insert++]=1;
                else
                    byteArray[insert++]=0;
            }
        }

//        for(int i=1; i<byteArray.length-12 && i<deCompress.size(); i++){
//            String temp = deCompress.get(i);
//            for (int j=0 ; j<temp.length(); j++){
//                if(temp.charAt(j)=='1')
//                    byteArray[12+i-1]=1;
//                else
//                    byteArray[12+i-1]=0;
//            }
//        }
        //byteArray=newByteMaze;
        return 0;
    }
    private LinkedList<Pair<Integer,Integer>>getDictionary(ArrayList<Integer> array ,int sizeOfUnit)throws IOException{
        LinkedList<Pair<Integer,Integer>> dictionary = new LinkedList<>();
        int pos=13+sizeOfUnit;
//        in.read();
//        in.read();
//        in.read();
        dictionary.add(new Pair<>(0,0));
        while(pos<array.size()){
                int val=array.get(pos++);
                if(val==-1)
                    break;
                int p=array.get(pos++) & 0xFF;
                for(int i=sizeOfUnit-2; i>=1; i--)
                    p = (p <<(8)) | (array.get(pos++) & 0xFF);
                dictionary.add(new Pair<>(val,p));
        }
        return dictionary;
    }
}
