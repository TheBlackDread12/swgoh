package holmesm.swgoh.tb.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.AnalyzeDocumentRequest;
import com.amazonaws.services.textract.model.AnalyzeDocumentResult;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.BlockType;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.FeatureType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import holmesm.swgoh.tb.analysis.GuildRoster.PlayerName;

public class ImageConverter {
	
	private static final String AWS_KEY = "key";
	private static final String AWS_SECRET_KEY="secretkey";
	private static final String IMAGE_LOC_KEY="imageLocation";
	private static final String OUTPUT_DEST_KEY="outputLocation";
	
	private static final Set<String> REQUIRED_CONFIG = Sets.newHashSet(AWS_KEY,AWS_SECRET_KEY,IMAGE_LOC_KEY,OUTPUT_DEST_KEY);
	
	public static void main(String[] args) throws IOException {
		
		if(args.length==0) {
			System.out.println("Cannot run - must specify a configuration location");
			System.exit(1);
		}
		
		String configLocation = args[0];
		
		File config = new File(configLocation+"\\configuration");
		Map<String,String> configMap = readConfiguration(config);
		GuildRoster roster = new GuildRoster(configLocation+"\\guildRoster", configLocation+"\\nameOverrides");
		
		
		File images = new File(configMap.get(IMAGE_LOC_KEY));
		File[] allImages = images.listFiles();
		
		Map<String,PlayerName> playerNames = roster.getExpandedNames();
		Set<String> allNames = playerNames.keySet();
		Map<String,Integer> playerScores = Maps.newHashMap();
		for(String player:roster.getPrimaryNames()) {
			playerScores.put(player, 0);
		}
		
		ImageDataExtractor extractor = new ImageDataExtractor(configMap.get(AWS_KEY),configMap.get(AWS_SECRET_KEY));
		
		for(File image:allImages) {
			playerScores.putAll(extractor.convertImage(image, playerNames, allNames));
		}
		StringBuilder builder = new StringBuilder();
		for(PlayerName player:roster.getOrderedNames()) {
			builder.append("\n"+player.getPrimaryName()+","+playerScores.get(player.getPrimaryName()));
		}
		FileWriter writer = new FileWriter(new File(configMap.get(OUTPUT_DEST_KEY)+"\\"+System.currentTimeMillis()));
		writer.write(builder.toString());
		writer.flush();
		writer.close();
		
		
		
	}
	
	
	private static Map<String,String> readConfiguration(File config) throws IOException{
		Map<String,String> returnMap = Maps.newHashMap();
		BufferedReader reader = new BufferedReader(new FileReader(config));
		String line = reader.readLine();
		while(line!=null) {
			String[] entry = line.split("=");
			if(entry.length!=2) {
				throw new RuntimeException("Invalid configuration line:"+line);
			}
			
			returnMap.put(entry[0], entry[1]);
			line = reader.readLine();
		}
		reader.close();
		
		for(String key:REQUIRED_CONFIG) {
			if(!returnMap.containsKey(key)) {
				throw new RuntimeException("Missing configuration:"+key);
			}
		}
		
		
		return returnMap;
	}
}
