package com.code.yilmaz;

import java.io.*;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;


public class WordCount {
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		while(tokenizer.hasMoreTokens()) {
				value.set(tokenizer.nextToken());
				context.write(value,new IntWritable(1));
	}
	
}
}
	public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable>
{
	public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
		int sum=0;
		for(IntWritable x:values)
		{
			sum+=x.get();
		}
		context.write(key,new IntWritable(sum));
	}
}

public static void main(String args[]) throws Exception
{
	Configuration conf= new Configuration();
	Job job = new Job(conf,"My Word Count Program");
	job.setJarByClass(WordCount.class);
	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);
	job.setInputFormatClass(TextInputFormat.class);
	job.setOutputFormatClass(TextOutputFormat.class);
	FileInputFormat.addInputPath(job,new Path(args[0]));
	FileOutputFormat.setOutputPath(job,new Path(args[1]));
	boolean result = job.waitForCompletion(true);
    System.exit(result?0:1);
}
}