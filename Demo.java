import java.lang.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.io.*;
import java.security.MessageDigest;

class Demo
{
	public static void main(String args[]) throws Exception
    {
        System.out.println("Enter the Dirctory Name");
        Scanner sobj = new Scanner(System.in);

        String dir = sobj.nextLine();
        cleaner cobj = new cleaner(dir);
        cobj.CleanDirectoryEmptyFile();

        cobj.CleanDirectoryDuplicateFile();
    }
}

class cleaner
{
    public File Fdir = null;

    public cleaner(String DirName)
    {
        Fdir = new File(DirName);
        
        if(!Fdir.exists())
        {
            System.out.println("Invalid Directory name");
            System.exit(0);
        }
    }
    
    public void CleanDirectoryEmptyFile()
    {
        File[] filelist = Fdir.listFiles();
        int EmptyFile = 0;

        for(File file : filelist)
        {
            if(file.length() == 0)
            {
                System.out.println("Empty file name "+file.getName());
                
                if(!file.delete())
                {
                    System.out.println("Unable to delete");
                }
                else
                {
                    EmptyFile++;
                }
            }   
        }
        System.out.println("Total empty files deleted : "+ EmptyFile);
    }

    public void CleanDirectoryDuplicateFile() throws Exception
    {
        // List all files from directory
        File filelist[] = Fdir.listFiles();

        // Counter to count number of duplicate files
        int DupFile = 0;
        
        // Bucket to read the data or array of byte
        byte bytearr[] = new byte[1024];
        
        // Create linkedlist of strings to store the checksum
        LinkedList<String> lobj = new LinkedList<String>();

        // Counter to read the data from file
        int Rcount = 0;

        MessageDigest digest = MessageDigest.getInstance("MD5");
        
        if(digest == null)
        {
            System.out.println("Unable to get the MD5");
            System.exit(0);
        }
        
        try{
        for(File file : filelist)
        {
            // Object to read the data from file
            FileInputStream fileReader = new FileInputStream(file);

            if(file.length() != 0)
            {
                while((Rcount = fileReader.read(bytearr)) != -1)
                {
                    digest.update(bytearr,0,Rcount);
                }
            }
            // to get the hash bytes of cheksum
            byte bytes[] = digest.digest();

            StringBuilder sb = new StringBuilder();
                //the stringbuffer
            for(int i = 0; i < bytes.length; i++)
            {
                // Add each byte from decimal to hexadecimal in
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("File name : " + file.getName()+"Checksum : "+ sb);

        

            if(lobj.contains(sb.toString()))
            {
                if(!file.delete())
                {
                    System.out.println("Unable to delete file : "+file.getName());
                }
                else
                {
                    System.out.println("File gets deleted :"+ file.getName());
                    DupFile++;
                }
            }
            else
            {
                lobj.add(sb.toString());
            }
                fileReader.close();

            }
        }
        catch(Exception obj)
        {
            System.out.println("Exception occured : "+obj);
        }
        finally
        {
        }
        System.out.println("Total duplicate files deleted : "+ DupFile);
    }
}