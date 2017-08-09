/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.explorer.importer;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author short
 */
public class SFTPConnector {

    private JSch jsch;
    private String server, user, pass;
    private int port;
    private Session session;
    private Channel channel;
    private ChannelSftp sftpChan;

    public SFTPConnector() {
	jsch = new JSch();

	server = "146.187.134.38";
	port = 22;
	user = "imgtech";
	pass = "$xP7*-Md@8H";
	try {
	    session = jsch.getSession(user, server, port);
	    session.setOutputStream(System.out);
	    //login with pw
	    session.setPassword(pass);
	    //don't intend to use key values for security
	    session.setConfig("StrictHostKeyChecking", "no");
	    session.connect();

	    channel = session.openChannel("sftp");
	    channel.connect();
	    sftpChan = (ChannelSftp) channel;
	} catch (JSchException e) {
	    System.out.println("Exception caught in sftp upload\n");
	    e.printStackTrace();
	}

    }

    public void uploadFile(InputStream is, String fileName, boolean isThumbnail) {
	try {
	    //create session with our user credentials

	    //moves to thumbnail directory if specified
	    if (isThumbnail) {
		sftpChan.cd("thumbnails");
	    }

	    //push file to server
	    sftpChan.put(is, fileName);
	    is.close();
	    
	    if(isThumbnail)
		sftpChan.cd("..");

	    //Disconnect all channels and session
//	    sftpChan.disconnect();
//	    channel.disconnect();
//	    session.disconnect();
	} catch (SftpException | IOException e) {
	    System.out.println("Exception caught in sftp upload\n");
	    e.printStackTrace();
	}
    }
    
    public void close()
    {
	sftpChan.disconnect();
	channel.disconnect();
	session.disconnect();
    }
}
