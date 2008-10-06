package jlm.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jlm.bugglequest.World;


public class Utils {

	public static void writeWorldIntoFile(World world, File outputFile) throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;
		MapWriter mw = null;
		try {
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			mw = new MapWriter(bw);
			mw.write(world);
		} catch (IOException e) {
			throw e;
		} finally {
			if (mw != null) mw.close();
			if (bw != null)  bw.close();
		}
	}

	public static void readWorldFromFile(World world, File inputFile) throws IOException {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);
			readWorldFromFile(world, br);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) br.close();
		}
	}

	public static void readWorldFromFile(World world, BufferedReader reader) throws IOException {
		MapReader mr = new MapReader(reader);
		mr.read(world);
	}
}
