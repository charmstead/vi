package com.slackbot.viaeai.viaeaibotMessage;

import com.slackbot.viaeai.viaeaibotMessageTypes.MessageType;
import java.io.Serializable;


public class Message implements Serializable
{		
	// members //
	protected long id;
	protected long creatorId;
	protected boolean isBot;
	protected String body;
	protected boolean isFile;
	protected String fileURL;
	protected String time;
	protected MessageType type;
	// end - members //
	
	// full constructor
	public Message(long newId, long newCreatorId, boolean newIsBot, String newBody, boolean newIsFile,
					String newFileURL, String newTime, MessageType newType)
	{
		this.id = newId;
		this.creatorId = newCreatorId;
		this.body = newBody;
		this.isFile = newIsFile;
		this.fileURL = newFileURL;
		this.time = newTime;
		this.type = newType;
		this.isBot = newIsBot;
	}
	
        public Message(){
            
        }
	///////////////////
	// get functions //
	///////////////////	
	
	// check if this message and the other one is conseptly the same
	public boolean isSame(Message mess)
	{
		if(this.id == mess.getId() && this.body.equals(mess.getBody()) 
				&& this.isFile == mess.isFile())
		{
			return true;
		}
		return false;
	}
	
	public long getId() 
	{
		return this.id;
	}
	
	public MessageType getType()
	{
            return this.type;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public long getCreatorId() 
	{
		return this.creatorId;
	}
	
	public String getBody() 
	{
		return this.body;
	}
	
	public boolean isFile()
	{
		return this.isFile;
	}
	
	public boolean isBot()
	{
		return this.isBot;
	}
	
	public String getFileURL()
	{
		return this.fileURL;
	}
	// --- get functions --- //
	
	///////////////////
	// set functions //
	///////////////////
        //Added an object builder to the setter method
        //rather than using farter constructor
	public Message setMessageId(long newMessageId)
	{
		this.id = newMessageId;
                return this;
	}
	
	public Message setCreatorId(long newCreatorId) 
	{
		this.creatorId = newCreatorId;
                return this;
	}
	
	public Message setMessageBody(String newMessageBody)
	{
		this.body = newMessageBody;
                return this;
	}
	
	public Message setMessage_time(String newTime)
	{
		this.time = newTime;
                return this;
	}
	
	public Message setIsBot(boolean newIsBot)
	{
		this.isBot = newIsBot;
                return this;
	}
        
        public Message setMessageType(MessageType messageType)
	{
		this.type = messageType;
                return this;
	}
        
        public Message setFileUrl(String url)
	{
		this.fileURL = url;
                return this;
	}
        
        public Message setIsFile(boolean isfile)
	{
		this.isFile = isfile;
                return this;
	}
	// --- set functions --- //
	
	/////////////////////////
	// operation functions //
	/////////////////////////
	
	// input: file URL
	// output: the Type of the file (our types logic)
	// action: get the file format and then find the right group it belongs to
	public static MessageType getFileType(String fileURL)
	{
		int counter = 0;
		// find the first place we have '.' from the end
		for(int i = fileURL.length() - 1; i >= 0; i--)
		{
			// count this char as well
			if(fileURL.charAt(i) != '.')
			{
				counter++;
			}
			else // if '.' break the loop
			{
				break;
			}
		}
		String typeText = fileURL.substring(fileURL.length() - counter, fileURL.length()).toLowerCase();
		// check which group we handle
		if(typeText.equals("png") || typeText.equals("teff") || typeText.equals("jpeg") || typeText.equals("jpg") || typeText.equals("gif"))
		{
			return MessageType.image;
		}
		else if(typeText.equals("wmv") || typeText.equals("mp3"))
		{
			return MessageType.audio;
		}
		else if(typeText.equals("mp4"))
		{
			return MessageType.video;
		}
		else if(typeText.equals("txt") || typeText.equals("pdf"))
		{
			return MessageType.document;
		}
		else
		{
			return MessageType.text;
		}
	}
	
	// input: file URL
	// output: the name of the file
	// action: get the file name from the url format
	public static String getFileName(String fileURL)
	{
		return  fileURL.substring(fileURL.lastIndexOf('/')+1, fileURL.length());
	}

	@Override
	public String toString() 
	{
		return "[id = " + id + ", creatorId = " + creatorId + ", body = " + body + ", isFile = " + isFile + ", fileURL = "
				+ fileURL + ", time = " + time + ", type = " + type + "]";
	}
	
	// --- end operation functions --- //
}


