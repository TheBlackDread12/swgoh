package holmesm.swgoh.tb.analysis;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
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

import holmesm.swgoh.tb.analysis.GuildRoster.PlayerName;

public class ImageDataExtractor {
	
	private static final String TEXTRACT_REGION = "us-east-1";
	private static final String TEXTRACT_ENDPOINT = "https://textract.us-east-1.amazonaws.com";

	private AmazonTextract client;
	private AWSCredentialsProvider credProvider;
	
	public ImageDataExtractor(String key,String secretKey) {
		
		credProvider =  new AWSStaticCredentialsProvider(new BasicAWSCredentials(key,secretKey));
		EndpointConfiguration endpoint = new EndpointConfiguration(
                TEXTRACT_ENDPOINT, TEXTRACT_REGION);
         client = AmazonTextractClientBuilder.standard()
                .withEndpointConfiguration(endpoint).build();
        
	}
	
	public Map<String,Integer> convertImage(File image, Map<String,PlayerName> playerNames, Set<String> allNames) throws IOException {
		
		AnalyzeDocumentRequest request = new AnalyzeDocumentRequest();
        request.setRequestCredentialsProvider(credProvider);
        request.setFeatureTypes(Lists.newArrayList(FeatureType.TABLES.toString()));
        Document doc = new Document();
		doc.setBytes(ByteBuffer.wrap(FileUtils.readFileToByteArray(image)));
		request.setDocument(doc);
		
		AnalyzeDocumentResult result = client.analyzeDocument(request);
		
		
		 Map<String,Integer> extractedResults = Maps.newHashMap();
		
		String currentName = null;
		for(Block block: result.getBlocks()) {
			
			if(BlockType.LINE.toString().equals(block.getBlockType())) {
				System.out.println(block.getText());
				String text = block.getText().replaceAll(",", "").toLowerCase();
				if(currentName==null && allNames.contains(text)) {
					currentName = text;
						System.out.println("Read in:"+currentName);
						continue;
				}
				if(currentName!=null && isScore(text)) {
					PlayerName resolvedName = playerNames.get(currentName);
					extractedResults.put(resolvedName.getPrimaryName(), Integer.parseInt(text));
					System.out.println("player score:"+currentName+","+text);
					currentName = null;
				}
			}
		}
		return extractedResults;
	}
	
	
	private static boolean isScore(String score) {
		try {
		int scoreInt = Integer.parseInt(score);
		return scoreInt>1000 || scoreInt==0;
		}
		catch(Exception e) {
			return false;
		}
		
	}
}
