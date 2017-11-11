package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.dazkins.triad.math.Matrix3;
import com.dazkins.triad.util.StringUtil;
import com.dazkins.triad.util.TriadLogger;

public class ShaderProgram
{
	public static ShaderProgram DEFAULT_SHADER;
	
	private static ShaderProgram CURRENT_SHADER;
	
	private int shaderProgram;
	
	public static void setupShaders()
	{
		DEFAULT_SHADER = new ShaderProgram("/shaders/vertexShader.glsl", "/shaders/fragmentShader.glsl");
	}
	
	public ShaderProgram(String v, String f)
	{
		String vertexContent = StringUtil.loadFileAsString(v);
		String fragmentContent = StringUtil.loadFileAsString(f);
		
		shaderProgram = GL20.glCreateProgram();
		
		int vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(vertID, vertexContent);
		GL20.glShaderSource(fragID, fragmentContent);
		
		GL20.glCompileShader(vertID);
		
		if (GL20.glGetShaderi(vertID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			TriadLogger.log("Failed to compile vertex shader!", true);
			TriadLogger.log(GL20.glGetShaderInfoLog(vertID), true);
		}
		
		GL20.glCompileShader(fragID);
		
		if (GL20.glGetShaderi(fragID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			TriadLogger.log("Failed to compile fragment shader!", true);
			TriadLogger.log(GL20.glGetShaderInfoLog(fragID), true);
		}
		
		GL20.glAttachShader(shaderProgram, vertID);
		GL20.glAttachShader(shaderProgram, fragID);
		
		GL20.glLinkProgram(shaderProgram);
		if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
		{
			TriadLogger.log("Failed to link shader program!", true);
			TriadLogger.log(GL20.glGetProgramInfoLog(shaderProgram), true);
		}
		
		GL20.glValidateProgram(shaderProgram);
		
		TriadLogger.log("Succesfully compiled shader!", false);
	}
	
	public void setUniform(String s, int val)
	{
//		bind();
		int loc = GL20.glGetUniformLocation(shaderProgram, s);
		GL20.glUniform1i(loc, val);
//		unbind();
	}
	
	public void setUniform(String s, Matrix3 m)
	{
		FloatBuffer b = m.dropToBuffer();
		int loc = GL20.glGetUniformLocation(shaderProgram, s);
		GL20.glUniformMatrix3fv(loc, false, b);
	}
	
	public static ShaderProgram getCurrentShader()
	{
		return CURRENT_SHADER;
	}
	
	public void bind()
	{
		if (CURRENT_SHADER != this)
		{
			GL20.glUseProgram(shaderProgram);
			CURRENT_SHADER = this;
		}
	}
	
	public void unbind()
	{
		GL20.glUseProgram(0);
		CURRENT_SHADER = null;
	}
}