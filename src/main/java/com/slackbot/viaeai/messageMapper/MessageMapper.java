/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slackbot.viaeai.messageMapper;


import com.slackbot.viaeai.viaeaibotMessageTypes.MessageType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import static java.util.Objects.isNull;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *This is the messageMapper class. 
 * NB: There several ways slack messages comes into your application
 * Namely: slashCommand/WebHooks and Events(RTM).
 * This messages will be mapped differently.
 * @author TOMIDE
 */
@Service
public class MessageMapper {
    
    
    private final Logger log = LoggerFactory.getLogger(MessageMapper.class);
    
    /**
     * This method maps slack incoming message to your application via
     * /slashcommand to the Custom message type.
     * The param will have to be constructed from the RequestParameters
     * @param map Contain incoming message request.
     * @return 
     */
    public com.slackbot.viaeai.viaeaibotMessage.Message mapSlashCommandToViaeaiMessage(Map<String,String> map){
        
       com.slackbot.viaeai.viaeaibotMessage.Message message = new com.slackbot.viaeai.viaeaibotMessage.Message();
       
       String text = map.containsKey("text")?map.get("text"):"";
       message.setMessage_time(new Date()+"")
               .setIsFile(false)//message via slash command are always text
               .setIsBot(true)
               .setMessageBody(text)
               .setCreatorId(map.containsKey("userId")?
                       Long.parseLong(map.get("text").substring(1)):0);
       
        try{
            //checks if the message is a link
            URL url = new URL(text);
            message.setMessageType(MessageType.site);

        } catch (MalformedURLException ex) {
             message.setMessageType(MessageType.text);
             log.debug("The incoming message is not a link type");
        }
       
       return message;
    }
  
    
  /**
   * This methods maps input messages from slack event messages to viaeaiMessageType.
   * @param event The event to be processed for input messages.
   * @return viaeaiMessage
   */
  public com.slackbot.viaeai.viaeaibotMessage.Message mapEventToViaeaiMessage(Event event){
      com.slackbot.viaeai.viaeaibotMessage.Message message = new com.slackbot.viaeai.viaeaibotMessage.Message();
      
     // long userId = Long.parseLong(!isNull(event.getUserId())?event.getUserId().substring(1):0);
      
      message.setMessageId(event.getId())
             //.setCreatorId(userId)
             .setMessage_time(new Date().toString())
             .setMessageType(MessageType.text);
             
             
      if(event.isOk() || event.getType().toLowerCase().contains("ack")){
          //this is just an acknowledgement
          message.setMessageBody("This is an acknowledgement message");
      }
      //checks if the event contains file
      else if(!isNull(event.getFile()) || event.getType().toLowerCase().contains("file")){
          message.setFileUrl(event.getFile().getUrlPrivateDownload())
                  .setIsFile(true)
                  .setMessage_time(event.getFile().getTimestamp()+"")
                  .setMessageType(MessageType.document);
          return message;
      }
     else if(!isNull(event.getComment())){
        message.setMessage_time(event.getComment().getTimestamp()+"")
               .setMessageBody(event.getComment().getComment())
                .setIsBot(true);
                
        return message;
    }
    else if(!isNull(event.getMessage())){
        message.setMessageBody(event.getMessage().getText());
                       
    }
    else{
        message.setMessageBody(event.getText());
    }
      
      return message;
  }
  
  /**
   * This methods maps viaeaiMessage to Slack text only reponse Message
   * @param message
   * @return Message
   */
  public Message mapToSlackMessage(com.slackbot.viaeai.viaeaibotMessage.Message message){
      
      return new Message(message.getBody());
  }
  
  /**
   * This methods maps viaeaiMessage to slack response message that supports
   * attachments.
   * @param message
   * @return RichMessage
   */
  public RichMessage mapToSlackRichMessage(com.slackbot.viaeai.viaeaibotMessage.Message message){
      RichMessage richMessage = new RichMessage(message.getBody());
      
      if(message.isFile() && !isNull(message.getFileURL())){
          
          Attachment[] attachments = new Attachment[1];
          Attachment file= new Attachment();
          file.setImageUrl(message.getFileURL());
          attachments[0] = file;
          attachments[0].setText(message.getBody());
          richMessage.setAttachments(attachments);
        
      }
      return richMessage;
  }
    
}