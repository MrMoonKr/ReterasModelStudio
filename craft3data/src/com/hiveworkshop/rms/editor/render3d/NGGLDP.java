package com.hiveworkshop.rms.editor.render3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.hiveworkshop.wc3.gui.modelviewer.ViewerCamera;
import com.hiveworkshop.wc3.util.MathUtils;

/**
 * NGGLDP stands for "Not Good GL Design Practices". I am just trying to hack
 * this in for now based on the old legacy RMS code that was using sad, bad
 * fixed pipeline code (which can never render Reforged models accurately!)
 */
public class NGGLDP {
	private static final FixedFunctionPipeline fixedFunctionPipeline = new FixedFunctionPipeline();

	public static Pipeline pipeline = null;

	public static void setPipeline(final Pipeline userPipeline) {
		pipeline = userPipeline;
		pipeline.onGlobalPipelineSet();
	}

	public static void fixedFunction() {
		pipeline = fixedFunctionPipeline;
	}

	public static final class ShaderSwitchingPipeline implements Pipeline {
		private final List<Pipeline> allShaderPipelines;
		private Pipeline currentPipeline;

		public ShaderSwitchingPipeline(final List<Pipeline> allShaderPipelines) {
			this.allShaderPipelines = allShaderPipelines;
		}

		@Override
		public void setCurrentPipeline(final int index) {
			currentPipeline = allShaderPipelines.get(index);
			currentPipeline.onGlobalPipelineSet();
		}

		@Override
		public int getCurrentPipelineIndex() {
			return allShaderPipelines.indexOf(currentPipeline);
		}

		@Override
		public void glBegin(final int type) {
			currentPipeline.glBegin(type);
		}

		@Override
		public void onGlobalPipelineSet() {
			if (currentPipeline != null) {
				currentPipeline.onGlobalPipelineSet();
			}
		}

		@Override
		public void glVertex3f(final float x, final float y, final float z) {
			currentPipeline.glVertex3f(x, y, z);
		}

		@Override
		public void glEnd() {
			currentPipeline.glEnd();
		}

		@Override
		public void glPolygonMode(final int face, final int mode) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glPolygonMode(face, mode);
			}
		}

		@Override
		public void glColor4f(final float r, final float g, final float b, final float a) {
			currentPipeline.glColor4f(r, g, b, a);
		}

		@Override
		public void glFresnelTeamColor1f(final float v) {
			currentPipeline.glFresnelTeamColor1f(v);
		}

		@Override
		public void glFresnelOpacity1f(final float v) {
			currentPipeline.glFresnelOpacity1f(v);
		}

		@Override
		public void glEmissiveGain1f(final float renderEmissiveGain) {
			currentPipeline.glEmissiveGain1f(renderEmissiveGain);
		}

		@Override
		public void glFresnelColor3f(final float r, final float g, final float b) {
			currentPipeline.glFresnelColor3f(r, g, b);
		}

		@Override
		public void glNormal3f(final float x, final float y, final float z) {
			currentPipeline.glNormal3f(x, y, z);
		}

		@Override
		public void glTexCoord2f(final float u, final float v) {
			currentPipeline.glTexCoord2f(u, v);
		}

		@Override
		public void glColor3f(final float r, final float g, final float b) {
			currentPipeline.glColor3f(r, g, b);
		}

		@Override
		public void glColor4ub(final byte r, final byte g, final byte b, final byte a) {
			currentPipeline.glColor4ub(r, g, b, a);
		}

		@Override
		public void glLight(final int light, final int pname, final FloatBuffer params) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glLight(light, pname, params);
			}
		}

		@Override
		public void glRotatef(final float angle, final float axisX, final float axisY, final float axisZ) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glRotatef(angle, axisX, axisY, axisZ);
			}
		}

		@Override
		public void glScalef(final float x, final float y, final float z) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glScalef(x, y, z);
			}
		}

		@Override
		public void glTranslatef(final float x, final float y, final float z) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glTranslatef(x, y, z);
			}
		}

		@Override
		public void glOrtho(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin,
				final float zMax) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
			}
		}

		@Override
		public void gluPerspective(final float fovY, final float aspect, final float nearClip, final float farClip) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.gluPerspective(fovY, aspect, nearClip, farClip);
			}
		}

		@Override
		public void glLightModel(final int lightModel, final FloatBuffer ambientColor) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glLightModel(lightModel, ambientColor);
			}
		}

		@Override
		public void glMatrixMode(final int mode) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glMatrixMode(mode);
			}
		}

		@Override
		public void glLoadIdentity() {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glLoadIdentity();
			}
		}

		@Override
		public void glEnableIfNeeded(final int glEnum) {
//			for (final Pipeline pipeline : allShaderPipelines) {
//				pipeline.onGlobalPipelineSet();
//				pipeline.glEnableIfNeeded(glEnum);
//			}
//			currentPipeline.onGlobalPipelineSet();
			currentPipeline.glEnableIfNeeded(glEnum);
		}

		@Override
		public void glShadeModel(final int glFlat) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glShadeModel(glFlat);
			}
		}

		@Override
		public void glDisableIfNeeded(final int glEnum) {
//			for (final Pipeline pipeline : allShaderPipelines) {
//				pipeline.onGlobalPipelineSet();
//				pipeline.glDisableIfNeeded(glEnum);
//			}
//			currentPipeline.onGlobalPipelineSet();
			currentPipeline.glDisableIfNeeded(glEnum);
		}

		@Override
		public void prepareToBindTexture() {
			currentPipeline.prepareToBindTexture();
		}

		@Override
		public void glTangent4f(final float x, final float y, final float z, final float w) {
			currentPipeline.glTangent4f(x, y, z, w);
		}

		@Override
		public void glActiveHDTexture(final int textureUnit) {
			currentPipeline.glActiveHDTexture(textureUnit);
		}

		@Override
		public void glViewport(final int x, final int y, final int w, final int h) {
			// NOTE maybe feels like this should apply to all, but currently as an
			// implementation detail we don't need the loop
			currentPipeline.glViewport(x, y, w, h);
		}

		@Override
		public void glCamera(final ViewerCamera viewerCamera, final boolean usingModelCamera) {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.glCamera(viewerCamera, usingModelCamera);
			}
		}

		@Override
		public void discard() {
			for (final Pipeline pipeline : allShaderPipelines) {
				pipeline.discard();
			}
		}

	}

	/**
	 * SimpleDiffuseShaderPipeline is only for classic SD models of Warcraft 3
	 */
	public static final class SimpleDiffuseShaderPipeline implements Pipeline {
		private static final int STRIDE = 4 /* position */ + 4 /* normal */ + 2 /* uv */ + 4 /* color */;
		private static final int STRIDE_BYTES = STRIDE * Float.BYTES;
		private static final String vertexShader = "#version 330 core\r\n" + //
				"\r\n" + //
				"layout (location = 0) in vec4 a_position;\r\n" + //
				"layout (location = 1) in vec4 a_normal;\r\n" + //
				"layout (location = 2) in vec2 a_uv;\r\n" + //
				"layout (location = 3) in vec4 a_color;\r\n" + //
				"\r\n" + //
				"out vec2 v_uv;\r\n" + //
				"out vec4 v_color;\r\n" + //
				"\r\n" + //
				"uniform vec3 u_lightDirection;\r\n" + //
				"uniform int u_lightingEnabled;\r\n" + //
				"uniform int u_usingModelCamera;\r\n" + //
				"\r\n" + //
				"void main() {\r\n" + //
				"		gl_Position = a_position;\r\n" + //
				"		v_uv = a_uv;\r\n" + //
				"		v_color = a_color;\r\n" + //
				"		if(u_lightingEnabled != 0) {\r\n" + //
				"			vec3 lightFactorContribution = vec3(clamp(dot(a_normal.xyz, u_lightDirection), 0.0, 1.0));\r\n"
				+ //
				"		    if(u_usingModelCamera != 0) {\r\n" + //
				"			    v_color.rgb = v_color.rgb * clamp(lightFactorContribution + 0.3f, 0.0, 1.0);\r\n" + //
				"		    } else {\r\n" + //
				"			    v_color.rgb = v_color.rgb * clamp(lightFactorContribution * 1.3 + vec3(0.5f, 0.5f, 0.5f), 0.0, 1.0);\r\n"
				+ //
				"		    }\r\n" + //
				"		}\r\n" + //
				"}\r\n\0";
		private static final String fragmentShader = "#version 330 core\r\n" + //
				"\r\n" + //
				"uniform sampler2D u_texture;\r\n" + //
				"uniform int u_textureUsed;\r\n" + //
				"uniform int u_alphaTest;\r\n" + //
				"\r\n" + //
				"in vec2 v_uv;\r\n" + //
				"in vec4 v_color;\r\n" + //
				"\r\n" + //
				"out vec4 FragColor;\r\n" + //
				"\r\n" + //
				"void main() {\r\n" + // s
				"		vec4 color;\r\n" + //
				"		if(u_textureUsed != 0) {\r\n" + //
				"			vec4 texel = texture2D(u_texture, v_uv);\r\n" + //
				"			color = texel * v_color;\r\n" + //
				"		} else {\r\n" + //
				"			color = v_color;\r\n" + //
				"		}\r\n" + //
				"		if(u_alphaTest != 0 && color.a < 0.75) {\r\n" + //
				"			discard;\r\n" + //
				"		}\r\n" + //
				"		FragColor = color;\r\n" + //
				"}\r\n\0";
		private final Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
		private FloatBuffer pipelineVertexBuffer = ByteBuffer.allocateDirect(1024 * 4).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		private int vertexCount = 0;
		private int normalCount = 0;
		private int uvCount = 0;
		private int colorCount = 0;
		private int glBeginType;
		private int shaderProgram;
		private int vertexBufferObjectId, vertexArrayObjectId; // has nothing to do with "object id" of war3 models
		private boolean loaded = false;
		private final Matrix4f currentMatrix = new Matrix4f();
		{
			currentMatrix.setIdentity();
		}
		private int textureUsed = 0;
		private int alphaTest = 0;
		private int lightingEnabled = 1;

		public SimpleDiffuseShaderPipeline() {
			load();
		}

		private int createShader(final int shaderType, final String shaderSource) {
			final int shaderId = GL20.glCreateShader(shaderType);
			GL20.glShaderSource(shaderId, shaderSource);
			GL20.glCompileShader(shaderId);
			final int compileStatus = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
			if (compileStatus == GL11.GL_FALSE) {
				final String errorText = GL20.glGetShaderInfoLog(shaderId, 1024);
				System.err.println(errorText);
				throw new IllegalStateException(compileStatus + ": " + errorText);
			}
			return shaderId;
		}

		@Override
		public void glBegin(final int type) {
			pipelineVertexBuffer.clear();
			glBeginType = type;
			vertexCount = 0;
			uvCount = 0;
			normalCount = 0;
			colorCount = 0;
			switch (type) {
			case GL11.GL_TRIANGLES:
				break;
			case GL11.GL_QUADS:
				break;
			case GL11.GL_LINES:
				break;
			default:
				throw new IllegalArgumentException(Integer.toString(type));
			}
		}

		private void load() {
			final int vertexShaderId = createShader(GL20.GL_VERTEX_SHADER, vertexShader);
			final int fragmentShaderId = createShader(GL20.GL_FRAGMENT_SHADER, fragmentShader);
			shaderProgram = GL20.glCreateProgram();
			GL20.glAttachShader(shaderProgram, vertexShaderId);
			GL20.glAttachShader(shaderProgram, fragmentShaderId);
			GL20.glLinkProgram(shaderProgram);
			final int linkStatus = GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS);
			if (linkStatus == GL11.GL_FALSE) {
				final String errorText = GL20.glGetProgramInfoLog(shaderProgram, 1024);
				System.err.println(errorText);
				throw new IllegalStateException(linkStatus + ": " + errorText);
			}
			GL20.glDeleteShader(vertexShaderId);
			GL20.glDeleteShader(fragmentShaderId);

			vertexArrayObjectId = GL30.glGenVertexArrays();
			vertexBufferObjectId = GL15.glGenBuffers();
			GL30.glBindVertexArray(vertexArrayObjectId);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
			loaded = true;

			// GL20.glGetAttribLocation(shaderProgram, "a_position") ?
		}

		private void pushFloat(final int absoluteOffset, final float x) {
			ensureCapacity(absoluteOffset);
			pipelineVertexBuffer.put(absoluteOffset, x);
		}

		private void ensureCapacity(final int absoluteOffset) {
			if (pipelineVertexBuffer.capacity() <= absoluteOffset) {
				final FloatBuffer largerBuffer = ByteBuffer
						.allocateDirect(Math.max((absoluteOffset + 1) * 4, pipelineVertexBuffer.capacity() * 2 * 4))
						.order(ByteOrder.nativeOrder()).asFloatBuffer().clear();
				pipelineVertexBuffer.flip();
				largerBuffer.put(pipelineVertexBuffer);
				largerBuffer.clear();
				pipelineVertexBuffer = largerBuffer;
			}
		}

		@Override
		public void glVertex3f(final float x, final float y, final float z) {
			final int baseOffset = vertexCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			tempVec4.set(x, y, z, 1);
			Matrix4f.transform(currentMatrix, tempVec4, tempVec4);
			pushFloat(baseOffset + 0, tempVec4.x);
			pushFloat(baseOffset + 1, tempVec4.y);
			pushFloat(baseOffset + 2, tempVec4.z);
			pushFloat(baseOffset + 3, tempVec4.w);
			pushFloat(baseOffset + 10, color.x);
			pushFloat(baseOffset + 11, color.y);
			pushFloat(baseOffset + 12, color.z);
			pushFloat(baseOffset + 13, color.w);
			vertexCount++;
		}

		@Override
		public void glEnd() {
			GL30.glBindVertexArray(vertexArrayObjectId);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);

			pipelineVertexBuffer.position(vertexCount * STRIDE);
			pipelineVertexBuffer.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, pipelineVertexBuffer, GL15.GL_DYNAMIC_DRAW);

			GL20.glEnableVertexAttribArray(0);
			GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 4 * Float.BYTES);
			GL20.glEnableVertexAttribArray(2);
			GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, STRIDE_BYTES, 8 * Float.BYTES);
			GL20.glEnableVertexAttribArray(3);
			GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 10 * Float.BYTES);

			GL20.glUseProgram(shaderProgram);

			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_texture"), 0);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureUsed"), textureUsed);
			textureUsed = 0;
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_alphaTest"), alphaTest);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_lightingEnabled"), lightingEnabled);
			if (usingModelCamera) {
				// this one emulates UI\MiscData.txt light
				// (used in WC3 portraits, so it'll be wrong on "main menu" background models)
				tempVec4.set(0.3f, -0.3f, 0.25f, 0.0f);
				GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_usingModelCamera"), 1);
			}
			else {
				// this one emulates DNC model light
				// (used in WC3 game world view)
				tempVec4.set(-24.1937f, 30.4879f, 444.411f, 0.0f);
				GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_usingModelCamera"), 0);
			}
//			Matrix4f.transform(currentMatrix, tempVec4, tempVec4);
			tempVec4.normalise();
			GL20.glUniform3f(GL20.glGetUniformLocation(shaderProgram, "u_lightDirection"), tempVec4.x, tempVec4.y,
					tempVec4.z);
			GL11.glDrawArrays(glBeginType, 0, vertexCount);
			vertexCount = 0;
			uvCount = 0;
			normalCount = 0;
			colorCount = 0;
			pipelineVertexBuffer.clear();
		}

		@Override
		public void glPolygonMode(final int face, final int mode) {
			GL11.glPolygonMode(face, mode);
		}

		@Override
		public void glColor4f(final float r, final float g, final float b, final float a) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set(r, g, b, a);
			pushFloat(baseOffset + 10, color.x);
			pushFloat(baseOffset + 11, color.y);
			pushFloat(baseOffset + 12, color.z);
			pushFloat(baseOffset + 13, color.w);
			colorCount++;
		}

		@Override
		public void glNormal3f(final float x, final float y, final float z) {
			final int baseOffset = normalCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			tempVec4.set(x, y, z, 0);
			tempVec4.normalise();
			pushFloat(baseOffset + 4, tempVec4.x);
			pushFloat(baseOffset + 5, tempVec4.y);
			pushFloat(baseOffset + 6, tempVec4.z);
			pushFloat(baseOffset + 7, 0);
			normalCount++;
		}

		@Override
		public void glTexCoord2f(final float u, final float v) {
			final int baseOffset = uvCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			pushFloat(baseOffset + 8, u);
			pushFloat(baseOffset + 9, v);
			uvCount++;
		}

		@Override
		public void glColor3f(final float r, final float g, final float b) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set(r, g, b, color.w);
			pushFloat(baseOffset + 10, color.x);
			pushFloat(baseOffset + 11, color.y);
			pushFloat(baseOffset + 12, color.z);
			colorCount++;
		}

		@Override
		public void glColor4ub(final byte r, final byte g, final byte b, final byte a) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set((r & 0xFF) / 255f, (g & 0xFF) / 255f, (b & 0xFF) / 255f, (a & 0xFF) / 255f);
			pushFloat(baseOffset + 10, color.x);
			pushFloat(baseOffset + 11, color.y);
			pushFloat(baseOffset + 12, color.z);
			pushFloat(baseOffset + 13, color.w);
			colorCount++;
		}

		@Override
		public void glLight(final int light, final int pname, final FloatBuffer params) {

		}

		private final Quaternion tempQuat = new Quaternion();
		private final Matrix4f tempMat4 = new Matrix4f();
		private boolean usingModelCamera;

		@Override
		public void glRotatef(final float angle, final float axisX, final float axisY, final float axisZ) {
			tempVec3.set(axisX, axisY, axisZ);
			tempVec3.normalise();
			tempVec4.set(tempVec3.x, tempVec3.y, tempVec3.z, (float) Math.toRadians(angle));
			tempQuat.setFromAxisAngle(tempVec4);
			tempQuat.normalise();
			MathUtils.fromQuat(tempQuat, tempMat4);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glCamera(final ViewerCamera viewerCamera, final boolean usingModelCamera) {
			this.usingModelCamera = usingModelCamera;
			Matrix4f.mul(viewerCamera.viewProjectionMatrix, currentMatrix, currentMatrix);
		}

		private final Vector3f tempVec3 = new Vector3f();
		private final Vector4f tempVec4 = new Vector4f();
		private int matrixMode;

		@Override
		public void glScalef(final float x, final float y, final float z) {
			tempMat4.setIdentity();
			tempVec3.set(x, y, z);
			tempMat4.scale(tempVec3);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glTranslatef(final float x, final float y, final float z) {
			tempMat4.setIdentity();
			tempVec3.set(x, y, z);
			tempMat4.translate(tempVec3);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glOrtho(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin,
				final float zMax) {
			MathUtils.setOrtho(currentMatrix, xMin, xMax, yMin, yMax, zMin, zMax);
		}

		@Override
		public void gluPerspective(final float fovY, final float aspect, final float nearClip, final float farClip) {
			MathUtils.setPerspective(currentMatrix, (float) Math.toRadians(fovY), aspect, nearClip, farClip);
			// When we are not using fixed function pipeline, notably Perspective cannot be
			// expressed as a matrix due to the math, so to emulate legacy behavior we will
			// set a flag and divide by negative Z factor later.
		}

		@Override
		public void glLightModel(final int lightModel, final FloatBuffer ambientColor) {

		}

		@Override
		public void glMatrixMode(final int mode) {
			matrixMode = mode;

		}

		@Override
		public void glLoadIdentity() {
			if (matrixMode == GL11.GL_PROJECTION) {
				currentMatrix.setIdentity();
			} // else if it is set to GL_MODELVIEW we should be in a different mode, but I was
				// lazy and only made 1 matrix and so we skip it....
		}

		@Override
		public void glEnableIfNeeded(final int glEnum) {
			if (glEnum == GL11.GL_TEXTURE_2D) {
				textureUsed = 1;
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
			}
			else if (glEnum == GL11.GL_ALPHA_TEST) {
				alphaTest = 1;
			}
			else if (glEnum == GL11.GL_LIGHTING) {
				lightingEnabled = 1;
			}
		}

		@Override
		public void glShadeModel(final int mode) {
		}

		@Override
		public void glDisableIfNeeded(final int glEnum) {
			if (glEnum == GL11.GL_TEXTURE_2D) {
				textureUsed = 0;
				GL13.glActiveTexture(0);
			}
			else if (glEnum == GL11.GL_ALPHA_TEST) {
				alphaTest = 0;
			}
			else if (glEnum == GL11.GL_LIGHTING) {
				lightingEnabled = 0;
			}
		}

		@Override
		public void prepareToBindTexture() {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			textureUsed = 1;
		}

		@Override
		public void onGlobalPipelineSet() {
			GL30.glBindVertexArray(vertexArrayObjectId);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
		}

		@Override
		public void glTangent4f(final float x, final float y, final float z, final float w) {
			// tangents are not applicable to old style drawing
		}

		@Override
		public void glActiveHDTexture(final int textureUnit) {
			// TODO Auto-generated method stub

		}

		@Override
		public void glViewport(final int x, final int y, final int w, final int h) {
			GL11.glViewport(x, y, w, h);
		}

		@Override
		public void glFresnelColor3f(final float r, final float g, final float b) {
		}

		@Override
		public void glFresnelTeamColor1f(final float v) {
		}

		@Override
		public void glFresnelOpacity1f(final float v) {
		}

		@Override
		public void discard() {
			GL20.glDeleteProgram(shaderProgram);
		}

		@Override
		public void setCurrentPipeline(final int pipelineId) {
		}

		@Override
		public int getCurrentPipelineIndex() {
			return 0;
		}

		@Override
		public void glEmissiveGain1f(final float renderEmissiveGain) {
		}

	}

	/**
	 * HDDiffuseShaderPipeline is only for classic HD models of Reforged
	 */
	public static final class HDDiffuseShaderPipeline implements Pipeline {
		private static final int STRIDE = 4 /* position */ + 4 /* normal */ + 4 /* tangent */ + 2 /* uv */
				+ 4 /* color */ ;
		private static final int STRIDE_BYTES = STRIDE * Float.BYTES;
		private static final String vertexShader = "#version 330 core\r\n" + //
				"\r\n" + //
				"layout (location = 0) in vec4 a_position;\r\n" + //
				"layout (location = 1) in vec4 a_normal;\r\n" + //
				"layout (location = 2) in vec4 a_tangent;\r\n" + //
				"layout (location = 3) in vec2 a_uv;\r\n" + //
				"layout (location = 4) in vec4 a_color;\r\n" + //
				"\r\n" + //
				"uniform vec3 u_lightDirection;\r\n" + //
				"uniform vec3 u_viewPos;\r\n" + //
				"uniform mat4 u_projection;\r\n" + //
				"\r\n" + //
				"out vec2 v_uv;\r\n" + //
				"out vec4 v_color;\r\n" + //
				"out vec3 v_tangentLightPos;\r\n" + //
				"out vec3 v_tangentViewPos;\r\n" + //
				"out vec3 v_tangentFragPos;\r\n" + //
				"\r\n" + //
				"\r\n" + //
				"void main() {\r\n" + //
				"		gl_Position = u_projection * a_position;\r\n" + //
				"		v_uv = a_uv;\r\n" + //
				"		v_color = a_color;\r\n" + //
				"		vec3 tangent = normalize(a_tangent.xyz);\r\n" + //
				"		vec3 mormal = normalize(a_normal.xyz);\r\n" + //
				// this is supposed to re-orthogonalize per
				// https://learnopengl.com/Advanced-Lighting/Normal-Mapping although I'm
				// undecided if wc3 needs it
				"		tangent = normalize(tangent - dot(tangent, mormal.xyz) * mormal.xyz);\r\n" + //
				"		vec3 binormal = normalize(cross(mormal.xyz, tangent) * a_tangent.w);\r\n" + //
//				"		mat3 mv = mat3(u_projection);\r\n" + //
				"		mat3 TBN = transpose(mat3(tangent, binormal, mormal.xyz));\r\n" + //
				"		v_tangentLightPos = TBN * normalize(u_lightDirection).xyz;\r\n" + //
				"		v_tangentViewPos = TBN * u_viewPos;\r\n" + //
				"		v_tangentFragPos = TBN * (a_position).xyz;\r\n" + //
				"}\r\n\0";
		private static final String fragmentShader = "#version 330 core\r\n" + //
				"\r\n" + //
				"uniform sampler2D u_textureDiffuse;\r\n" + //
				"uniform sampler2D u_textureNormal;\r\n" + //
				"uniform sampler2D u_textureORM;\r\n" + //
				"uniform sampler2D u_textureEmissive;\r\n" + //
				"uniform sampler2D u_textureTeamColor;\r\n" + //
				"uniform sampler2D u_textureReflections;\r\n" + //
				"uniform int u_textureUsed;\r\n" + //
				"uniform int u_alphaTest;\r\n" + //
				"uniform int u_lightingEnabled;\r\n" + //
				"uniform float u_fresnelTeamColor;\r\n" + //
				"uniform float u_emissiveGain;\r\n" + //
				"uniform vec4 u_fresnelColor;\r\n" + //
				"uniform vec2 u_viewportSize;\r\n" //
				+ "" //
//				+ "float GeometrySchlickGGX(float NdotV, float k)\r\n" // Source: https://learnopengl.com/PBR/Theory
//				+ "{\r\n" //
//				+ "    float nom   = NdotV;\r\n" //
//				+ "    float denom = NdotV * (1.0 - k) + k;\r\n" //
//				+ "	\r\n" //
//				+ "    return nom / denom;\r\n" //
//				+ "}\r\n" //
//				+ "  \r\n" //
//				+ "float GeometrySmith(vec3 N, vec3 V, vec3 L, float k)\r\n" //
//				+ "{\r\n" //
//				+ "    float NdotV = max(dot(N, V), 0.0);\r\n" //
//				+ "    float NdotL = max(dot(N, L), 0.0);\r\n" //
//				+ "    float ggx1 = GeometrySchlickGGX(NdotV, k);\r\n" //
//				+ "    float ggx2 = GeometrySchlickGGX(NdotL, k);\r\n" //
//				+ "	\r\n" //
//				+ "    return ggx1 * ggx2;\r\n" //
//				+ "}\r\n" //
//				+ "" //
//				+ ""
//				+ "\r\n"
//				+ "float DistributionGGX(vec3 N, vec3 H, float a)\r\n"
//				+ "{\r\n"
//				+ "    float a2     = a*a;\r\n"
//				+ "    float NdotH  = max(dot(N, H), 0.0);\r\n"
//				+ "    float NdotH2 = NdotH*NdotH;\r\n"
//				+ "	\r\n"
//				+ "    float nom    = a2;\r\n"
//				+ "    float denom  = (NdotH2 * (a2 - 1.0) + 1.0);\r\n"
//				+ "    denom        = PI * denom * denom;\r\n"
//				+ "	\r\n"
//				+ "    return nom / denom;\r\n"
//				+ "}\r\n"
				+ "" + "" //
				+ "" + //
				"\r\n" + //
				"in vec2 v_uv;\r\n" + //
				"in vec4 v_color;\r\n" + //
				"in vec3 v_tangentLightPos;\r\n" + //
				"in vec3 v_tangentViewPos;\r\n" + //
				"in vec3 v_tangentFragPos;\r\n" + //
				"\r\n" + //
				"out vec4 FragColor;\r\n" + //
				"\r\n" + //
				"void main() {\r\n" + // s
				"		vec4 color;\r\n" + //
				"		vec4 ormTexel = texture2D(u_textureORM, v_uv);\r\n" + //
				"		vec4 teamColorTexel = texture2D(u_textureTeamColor, v_uv);\r\n" + //
				"		if(u_textureUsed != 0) {\r\n" + //
				"			vec4 texel = texture2D(u_textureDiffuse, v_uv);\r\n" + //
				"			color = vec4(texel.rgb * ((1.0 - ormTexel.a) + (teamColorTexel.rgb * ormTexel.a)), texel.a) * v_color;\r\n"
				+ //
				"		} else {\r\n" + //
				"			color = v_color;\r\n" + //
				"		}\r\n" + //
				"		if(v_color.a == 1.0 && u_alphaTest != 0 && color.a < 0.75) {\r\n" + //
				"			discard;\r\n" + //
				"		}\r\n" + //
				"		if(color.a == 0.0) {\r\n" + //
				"			discard;\r\n" + //
				"		}\r\n" + //
				"		if(u_lightingEnabled != 0) {\r\n" + //
				"			vec3 normalXYZ = texture2D(u_textureNormal, v_uv).xyz;\r\n" + //
				"			vec2 normalXY = normalXYZ.yx * 2.0 - 1.0;\r\n" + //
				"			vec3 normal = vec3(normalXY, sqrt(1.0 - dot(normalXY,normalXY)));\r\n" + //
				"			vec4 emissiveTexel = texture2D(u_textureEmissive, v_uv);\r\n" + //
				"			vec4 reflectionsTexel = clamp(0.2+2.0*texture2D(u_textureReflections, vec2(gl_FragCoord.x/u_viewportSize.x, -gl_FragCoord.y/u_viewportSize.y)), 0.0, 1.0);\r\n"
				+ //
				"			vec3 lightDir = normalize(v_tangentLightPos);\r\n" + //
				"			float cosTheta = dot(lightDir, normal) * 0.5 + 0.5;\r\n" + //
				"			float lambertFactor = clamp(cosTheta, 0.0, 1.0);\r\n" + //
				"			vec3 diffuse = (clamp(lambertFactor, 0.0, 1.0)) * color.xyz;\r\n" + //
				"			vec3 viewDir = normalize(v_tangentViewPos - v_tangentFragPos);\r\n" + //
				"			vec3 reflectDir = reflect(-lightDir, normal);\r\n" + //
				"			vec3 halfwayDir = normalize(lightDir + viewDir);\r\n" + //
				"			float spec = pow(max(dot(normal, halfwayDir)*0.5 + 0.5, 0.0), 32.0);\r\n" + //
				"			vec3 specular = vec3(max(-ormTexel.g+0.5, 0.0)+ormTexel.b) * spec * (reflectionsTexel.xyz * (1.0 - ormTexel.g) + ormTexel.g * color.xyz);\r\n"
				+ //
				"			vec3 fresnelColor = vec3(u_fresnelColor.rgb * (1.0 - u_fresnelTeamColor) + teamColorTexel.rgb *  u_fresnelTeamColor) * v_color.rgb;\r\n"
				+ //
				"			vec3 fresnel = fresnelColor*pow(1.0 - dot(normalize(v_tangentViewPos), normal), 1.0)*u_fresnelColor.a;\r\n"
				+ //
				"			FragColor = vec4(emissiveTexel.xyz * sqrt(u_emissiveGain) + specular + diffuse + fresnel, color.a);\r\n"
				+ //
				"		} else {\r\n" + //
				"			FragColor = color;\r\n" + //
				"		}\r\n" + //
				"}\r\n\0";
		private final Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
		private final Vector3f fresnelColor = new Vector3f(0f, 0f, 0f);
		private FloatBuffer pipelineVertexBuffer = ByteBuffer.allocateDirect(1024 * 4).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		private final FloatBuffer pipelineMatrixBuffer = ByteBuffer.allocateDirect(16 * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		private int vertexCount = 0;
		private int normalCount = 0;
		private int tangentCount = 0;
		private int uvCount = 0;
		private int colorCount = 0;
		private int fresnelColorCount = 0;
		private int glBeginType;
		private int shaderProgram;
		private int vertexBufferObjectId, vertexArrayObjectId; // has nothing to do with "object id" of war3 models
		private boolean loaded = false;
		private final Matrix4f currentMatrix = new Matrix4f();
		{
			currentMatrix.setIdentity();
		}
		private int textureUsed = 0;
		private int alphaTest = 0;
		private int lightingEnabled = 1;
		private float fresnelTeamColor;
		private float fresnelOpacity;
		private float renderEmissiveGain;

		public HDDiffuseShaderPipeline() {
			load();
		}

		private int createShader(final int shaderType, final String shaderSource) {
			final int shaderId = GL20.glCreateShader(shaderType);
			GL20.glShaderSource(shaderId, shaderSource);
			GL20.glCompileShader(shaderId);
			final int compileStatus = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
			if (compileStatus == GL11.GL_FALSE) {
				final String errorText = GL20.glGetShaderInfoLog(shaderId, 1024);
				System.err.println(errorText);
				throw new IllegalStateException(compileStatus + ": " + errorText);
			}
			return shaderId;
		}

		@Override
		public void glBegin(final int type) {
			pipelineVertexBuffer.clear();
			glBeginType = type;
			vertexCount = 0;
			uvCount = 0;
			normalCount = 0;
			colorCount = 0;
			fresnelColorCount = 0;
			tangentCount = 0;
			switch (type) {
			case GL11.GL_TRIANGLES:
				break;
			case GL11.GL_QUADS:
				break;
			case GL11.GL_LINES:
				break;
			default:
				throw new IllegalArgumentException(Integer.toString(type));
			}
		}

		private void load() {
			final int vertexShaderId = createShader(GL20.GL_VERTEX_SHADER, vertexShader);
			final int fragmentShaderId = createShader(GL20.GL_FRAGMENT_SHADER, fragmentShader);
			shaderProgram = GL20.glCreateProgram();
			GL20.glAttachShader(shaderProgram, vertexShaderId);
			GL20.glAttachShader(shaderProgram, fragmentShaderId);
			GL20.glLinkProgram(shaderProgram);
			final int linkStatus = GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS);
			if (linkStatus == GL11.GL_FALSE) {
				final String errorText = GL20.glGetProgramInfoLog(shaderProgram, 1024);
				System.err.println(errorText);
				throw new IllegalStateException(linkStatus + ": " + errorText);
			}
			GL20.glDeleteShader(vertexShaderId);
			GL20.glDeleteShader(fragmentShaderId);

			vertexArrayObjectId = GL30.glGenVertexArrays();
			vertexBufferObjectId = GL15.glGenBuffers();
			GL30.glBindVertexArray(vertexArrayObjectId);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
			loaded = true;

			// GL20.glGetAttribLocation(shaderProgram, "a_position") ?
		}

		private void pushFloat(final int absoluteOffset, final float x) {
			ensureCapacity(absoluteOffset);
			pipelineVertexBuffer.put(absoluteOffset, x);
		}

		private void ensureCapacity(final int absoluteOffset) {
			if (pipelineVertexBuffer.capacity() <= absoluteOffset) {
				final int newSizeBytes = Math.max((absoluteOffset + 1) * 4, pipelineVertexBuffer.capacity() * 2 * 4);
				final FloatBuffer largerBuffer = ByteBuffer.allocateDirect(newSizeBytes).order(ByteOrder.nativeOrder())
						.asFloatBuffer().clear();
				pipelineVertexBuffer.flip();
				largerBuffer.put(pipelineVertexBuffer);
				largerBuffer.clear();
				pipelineVertexBuffer = largerBuffer;
			}
		}

		@Override
		public void glVertex3f(final float x, final float y, final float z) {
			final int baseOffset = vertexCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			pushFloat(baseOffset + 0, x);
			pushFloat(baseOffset + 1, y);
			pushFloat(baseOffset + 2, z);
			pushFloat(baseOffset + 3, 1);
			pushFloat(baseOffset + 14, color.x);
			pushFloat(baseOffset + 15, color.y);
			pushFloat(baseOffset + 16, color.z);
			pushFloat(baseOffset + 17, color.w);
			pushFloat(baseOffset + 18, fresnelColor.x);
			pushFloat(baseOffset + 19, fresnelColor.y);
			pushFloat(baseOffset + 20, fresnelColor.z);
			vertexCount++;
		}

		@Override
		public void glEnd() {
			GL30.glBindVertexArray(vertexArrayObjectId);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);

			pipelineVertexBuffer.position(vertexCount * STRIDE);
			pipelineVertexBuffer.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, pipelineVertexBuffer, GL15.GL_DYNAMIC_DRAW);

			GL20.glEnableVertexAttribArray(0);
			GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 4 * Float.BYTES);
			GL20.glEnableVertexAttribArray(2);
			GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 8 * Float.BYTES);
			GL20.glEnableVertexAttribArray(3);
			GL20.glVertexAttribPointer(3, 2, GL11.GL_FLOAT, false, STRIDE_BYTES, 12 * Float.BYTES);
			GL20.glEnableVertexAttribArray(4);
			GL20.glVertexAttribPointer(4, 4, GL11.GL_FLOAT, false, STRIDE_BYTES, 14 * Float.BYTES);
			GL20.glEnableVertexAttribArray(5);
			GL20.glVertexAttribPointer(5, 3, GL11.GL_FLOAT, false, STRIDE_BYTES, 18 * Float.BYTES);

			GL20.glUseProgram(shaderProgram);

			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureDiffuse"), 0);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureNormal"), 1);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureORM"), 2);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureEmissive"), 3);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureTeamColor"), 4);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureReflections"), 5);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_textureUsed"), textureUsed);
			textureUsed = 0;
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_alphaTest"), alphaTest);
			GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "u_lightingEnabled"), lightingEnabled);

			if (usingModelCamera) {
				// this one emulates UI\MiscData.txt light
				// (used in WC3 portraits, so it'll be wrong on "main menu" background models)
				GL20.glUniform3f(GL20.glGetUniformLocation(shaderProgram, "u_lightDirection"), 0.3f, -0.3f, 0.25f);
			}
			else {
				// this one emulates DNC model light
				// (used in WC3 game world view)
				GL20.glUniform3f(GL20.glGetUniformLocation(shaderProgram, "u_lightDirection"), -24.1937f, 30.4879f,
						444.411f);
			}

//			GL20.glUniform3f(GL20.glGetUniformLocation(shaderProgram, "u_lightDirection"), 0.0f, 0.0f, -10000f);

			GL20.glUniform3f(GL20.glGetUniformLocation(shaderProgram, "u_viewPos"), cameraLocation.x, cameraLocation.y,
					cameraLocation.z);
			GL20.glUniform2f(GL20.glGetUniformLocation(shaderProgram, "u_viewportSize"), viewportWidth, viewportHeight);
			GL20.glUniform1f(GL20.glGetUniformLocation(shaderProgram, "u_fresnelTeamColor"), fresnelTeamColor);
			GL20.glUniform4f(GL20.glGetUniformLocation(shaderProgram, "u_fresnelColor"), fresnelColor.x, fresnelColor.y,
					fresnelColor.z, fresnelOpacity);
			GL20.glUniform1f(GL20.glGetUniformLocation(shaderProgram, "u_emissiveGain"), renderEmissiveGain);
			pipelineMatrixBuffer.clear();
			pipelineMatrixBuffer.put(currentMatrix.m00);
			pipelineMatrixBuffer.put(currentMatrix.m01);
			pipelineMatrixBuffer.put(currentMatrix.m02);
			pipelineMatrixBuffer.put(currentMatrix.m03);
			pipelineMatrixBuffer.put(currentMatrix.m10);
			pipelineMatrixBuffer.put(currentMatrix.m11);
			pipelineMatrixBuffer.put(currentMatrix.m12);
			pipelineMatrixBuffer.put(currentMatrix.m13);
			pipelineMatrixBuffer.put(currentMatrix.m20);
			pipelineMatrixBuffer.put(currentMatrix.m21);
			pipelineMatrixBuffer.put(currentMatrix.m22);
			pipelineMatrixBuffer.put(currentMatrix.m23);
			pipelineMatrixBuffer.put(currentMatrix.m30);
			pipelineMatrixBuffer.put(currentMatrix.m31);
			pipelineMatrixBuffer.put(currentMatrix.m32);
			pipelineMatrixBuffer.put(currentMatrix.m33);
			pipelineMatrixBuffer.flip();
//			pipelineMatrixBuffer.put(currentMatrix.m00);
//			pipelineMatrixBuffer.put(currentMatrix.m10);
//			pipelineMatrixBuffer.put(currentMatrix.m20);
//			pipelineMatrixBuffer.put(currentMatrix.m30);
//			pipelineMatrixBuffer.put(currentMatrix.m01);
//			pipelineMatrixBuffer.put(currentMatrix.m11);
//			pipelineMatrixBuffer.put(currentMatrix.m21);
//			pipelineMatrixBuffer.put(currentMatrix.m31);
//			pipelineMatrixBuffer.put(currentMatrix.m02);
//			pipelineMatrixBuffer.put(currentMatrix.m12);
//			pipelineMatrixBuffer.put(currentMatrix.m22);
//			pipelineMatrixBuffer.put(currentMatrix.m32);
//			pipelineMatrixBuffer.put(currentMatrix.m03);
//			pipelineMatrixBuffer.put(currentMatrix.m13);
//			pipelineMatrixBuffer.put(currentMatrix.m23);
//			pipelineMatrixBuffer.put(currentMatrix.m33);
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(shaderProgram, "u_projection"), false,
					pipelineMatrixBuffer);
			GL11.glDrawArrays(glBeginType, 0, vertexCount);
			vertexCount = 0;
			uvCount = 0;
			normalCount = 0;
			colorCount = 0;
			fresnelColorCount = 0;
			tangentCount = 0;
			pipelineVertexBuffer.clear();
		}

		@Override
		public void glPolygonMode(final int face, final int mode) {
			GL11.glPolygonMode(face, mode);
		}

		@Override
		public void glColor4f(final float r, final float g, final float b, final float a) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set(r, g, b, a);
			pushFloat(baseOffset + 14, color.x);
			pushFloat(baseOffset + 15, color.y);
			pushFloat(baseOffset + 16, color.z);
			pushFloat(baseOffset + 17, color.w);
			colorCount++;
		}

		@Override
		public void glNormal3f(final float x, final float y, final float z) {
			final int baseOffset = normalCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			pushFloat(baseOffset + 4, x);
			pushFloat(baseOffset + 5, y);
			pushFloat(baseOffset + 6, z);
			pushFloat(baseOffset + 7, 0);
			normalCount++;
		}

		@Override
		public void glTexCoord2f(final float u, final float v) {
			final int baseOffset = uvCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			pushFloat(baseOffset + 12, u);
			pushFloat(baseOffset + 13, v);
			uvCount++;
		}

		@Override
		public void glColor3f(final float r, final float g, final float b) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set(r, g, b, color.w);
			pushFloat(baseOffset + 14, color.x);
			pushFloat(baseOffset + 15, color.y);
			pushFloat(baseOffset + 16, color.z);
			colorCount++;
		}

		@Override
		public void glFresnelTeamColor1f(final float v) {
			fresnelTeamColor = v;
		}

		@Override
		public void glFresnelOpacity1f(final float v) {
			fresnelOpacity = v;
		}

		@Override
		public void glEmissiveGain1f(final float renderEmissiveGain) {
			this.renderEmissiveGain = renderEmissiveGain;
		}

		@Override
		public void glFresnelColor3f(final float r, final float g, final float b) {
			final int baseOffset = fresnelColorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			fresnelColor.set(r, g, b);
			pushFloat(baseOffset + 18, r);
			pushFloat(baseOffset + 19, g);
			pushFloat(baseOffset + 20, b);
			fresnelColorCount++;
		}

		@Override
		public void glColor4ub(final byte r, final byte g, final byte b, final byte a) {
			final int baseOffset = colorCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			color.set((r & 0xFF) / 255f, (g & 0xFF) / 255f, (b & 0xFF) / 255f, (a & 0xFF) / 255f);
			pushFloat(baseOffset + 14, color.x);
			pushFloat(baseOffset + 15, color.y);
			pushFloat(baseOffset + 16, color.z);
			pushFloat(baseOffset + 17, color.w);
			colorCount++;
		}

		@Override
		public void glLight(final int light, final int pname, final FloatBuffer params) {

		}

		private final Quaternion tempQuat = new Quaternion();
		private final Matrix4f tempMat4 = new Matrix4f();
		private boolean usingModelCamera;

		@Override
		public void glRotatef(final float angle, final float axisX, final float axisY, final float axisZ) {
			tempVec3.set(axisX, axisY, axisZ);
			tempVec3.normalise();
			tempVec4.set(tempVec3.x, tempVec3.y, tempVec3.z, (float) Math.toRadians(angle));
			tempQuat.setFromAxisAngle(tempVec4);
			tempQuat.normalise();
			MathUtils.fromQuat(tempQuat, tempMat4);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glCamera(final ViewerCamera viewerCamera, final boolean usingModelCamera) {
			this.usingModelCamera = usingModelCamera;
			cameraLocation.set(viewerCamera.location);
			Matrix4f.mul(viewerCamera.viewProjectionMatrix, currentMatrix, currentMatrix);
		}

		private final Vector3f tempVec3 = new Vector3f();
		private final Vector4f tempVec4 = new Vector4f();
		private int textureUnit;
		private int matrixMode;
		private int viewportWidth;
		private int viewportHeight;
		private final Vector3f cameraLocation = new Vector3f();

		@Override
		public void glScalef(final float x, final float y, final float z) {
			tempMat4.setIdentity();
			tempVec3.set(x, y, z);
			tempMat4.scale(tempVec3);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glTranslatef(final float x, final float y, final float z) {
			tempMat4.setIdentity();
			tempVec3.set(x, y, z);
			tempMat4.translate(tempVec3);
			Matrix4f.mul(currentMatrix, tempMat4, currentMatrix);
		}

		@Override
		public void glOrtho(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin,
				final float zMax) {
			MathUtils.setOrtho(currentMatrix, xMin, xMax, yMin, yMax, zMin, zMax);
		}

		@Override
		public void gluPerspective(final float fovY, final float aspect, final float nearClip, final float farClip) {
			MathUtils.setPerspective(currentMatrix, (float) Math.toRadians(fovY), aspect, nearClip, farClip);
			// When we are not using fixed function pipeline, notably Perspective cannot be
			// expressed as a matrix due to the math, so to emulate legacy behavior we will
			// set a flag and divide by negative Z factor later.
		}

		@Override
		public void glLightModel(final int lightModel, final FloatBuffer ambientColor) {

		}

		@Override
		public void glMatrixMode(final int mode) {
			matrixMode = mode;

		}

		@Override
		public void glLoadIdentity() {
			if (matrixMode == GL11.GL_PROJECTION) {
				currentMatrix.setIdentity();
			} // else if it is set to GL_MODELVIEW we should be in a different mode, but I was
				// lazy and only made 1 matrix and so we skip it....
		}

		@Override
		public void glEnableIfNeeded(final int glEnum) {
			if (glEnum == GL11.GL_TEXTURE_2D) {
				textureUsed = 1;
				GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
			}
			else if ((glEnum == GL11.GL_ALPHA_TEST) && (textureUnit == 0)) {
				alphaTest = 1;
			}
			else if (glEnum == GL11.GL_LIGHTING) {
				lightingEnabled = 1;
			}
		}

		@Override
		public void glShadeModel(final int mode) {
		}

		@Override
		public void glDisableIfNeeded(final int glEnum) {
			if (glEnum == GL11.GL_TEXTURE_2D) {
				textureUsed = 0;
				GL13.glActiveTexture(0);
			}
			else if ((glEnum == GL11.GL_ALPHA_TEST) && (textureUnit == 0)) {
				alphaTest = 0;
			}
			else if (glEnum == GL11.GL_LIGHTING) {
				lightingEnabled = 0;
			}
		}

		@Override
		public void prepareToBindTexture() {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
			textureUsed = 1;
		}

		@Override
		public void onGlobalPipelineSet() {
			GL30.glBindVertexArray(vertexArrayObjectId);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
		}

		@Override
		public void glTangent4f(final float x, final float y, final float z, final float w) {
			final int baseOffset = tangentCount * STRIDE;
			ensureCapacity(baseOffset + STRIDE);
			pushFloat(baseOffset + 8, x);
			pushFloat(baseOffset + 9, y);
			pushFloat(baseOffset + 10, z);
			pushFloat(baseOffset + 11, w);
			tangentCount++;
		}

		@Override
		public void glActiveHDTexture(final int textureUnit) {
			this.textureUnit = textureUnit;
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
		}

		@Override
		public void glViewport(final int x, final int y, final int w, final int h) {
			viewportWidth = w;
			viewportHeight = h;
			GL11.glViewport(x, y, w, h);
		}

		@Override
		public void discard() {
			GL20.glDeleteProgram(shaderProgram);
		}

		@Override
		public void setCurrentPipeline(final int pipelineId) {
		}

		@Override
		public int getCurrentPipelineIndex() {
			return 1;
		}

	}

	public static final class FixedFunctionPipeline implements Pipeline {
		@Override
		public void glBegin(final int type) {
			GL11.glBegin(type);
		}

		@Override
		public void glVertex3f(final float x, final float y, final float z) {
			GL11.glVertex3f(x, y, z);
		}

		@Override
		public void glEnd() {
			GL11.glEnd();
		}

		@Override
		public void glPolygonMode(final int face, final int mode) {
			GL11.glPolygonMode(face, mode);
		}

		@Override
		public void glColor4f(final float r, final float g, final float b, final float a) {
			GL11.glColor4f(r, g, b, a);
		}

		@Override
		public void glNormal3f(final float x, final float y, final float z) {
			GL11.glNormal3f(x, y, z);
		}

		@Override
		public void glTexCoord2f(final float u, final float v) {
			GL11.glTexCoord2f(u, v);
		}

		@Override
		public void glColor3f(final float r, final float g, final float b) {
			GL11.glColor3f(r, g, b);
		}

		@Override
		public void glFresnelColor3f(final float r, final float g, final float b) {
		}

		@Override
		public void glColor4ub(final byte r, final byte g, final byte b, final byte a) {
			GL11.glColor4ub(r, g, b, a);
		}

		@Override
		public void glLight(final int light, final int pname, final FloatBuffer params) {
			GL11.glLight(light, pname, params);
		}

		@Override
		public void glRotatef(final float a, final float b, final float c, final float d) {
			GL11.glRotatef(a, b, c, d);
		}

		@Override
		public void glScalef(final float x, final float y, final float z) {
			GL11.glScalef(x, y, z);
		}

		@Override
		public void glTranslatef(final float x, final float y, final float z) {
			GL11.glTranslatef(x, y, z);
		}

		@Override
		public void glOrtho(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin,
				final float zMax) {
			GL11.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
		}

		@Override
		public void gluPerspective(final float fovY, final float aspect, final float nearClip, final float farClip) {
			GLU.gluPerspective(fovY, aspect, nearClip, farClip);
		}

		@Override
		public void glLightModel(final int lightModel, final FloatBuffer ambientColor) {
			GL11.glLightModel(lightModel, ambientColor);
		}

		@Override
		public void glMatrixMode(final int mode) {
			GL11.glMatrixMode(mode);
		}

		@Override
		public void glLoadIdentity() {
			GL11.glLoadIdentity();
		}

		@Override
		public void glEnableIfNeeded(final int glEnum) {
			GL11.glEnable(glEnum);
		}

		@Override
		public void glShadeModel(final int glFlat) {
			GL11.glShadeModel(glFlat);
		}

		@Override
		public void glDisableIfNeeded(final int glEnum) {
			GL11.glDisable(glEnum);
		}

		@Override
		public void prepareToBindTexture() {
		}

		@Override
		public void onGlobalPipelineSet() {
		}

		@Override
		public void glTangent4f(final float x, final float y, final float z, final float w) {
			// tangents are not applicable to old style drawing
		}

		@Override
		public void glActiveHDTexture(final int textureUnit) {
			// TODO Auto-generated method stub

		}

		@Override
		public void glViewport(final int x, final int y, final int w, final int h) {
			GL11.glViewport(x, y, w, h);
		}

		@Override
		public void glFresnelTeamColor1f(final float v) {
		}

		@Override
		public void glFresnelOpacity1f(final float v) {
		}

		@Override
		public void glCamera(final ViewerCamera viewerCamera, final boolean usingModelCamera) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void discard() {
		}

		@Override
		public void setCurrentPipeline(final int pipelineId) {
		}

		@Override
		public int getCurrentPipelineIndex() {
			return 0;
		}

		@Override
		public void glEmissiveGain1f(final float renderEmissiveGain) {
		}

	}

	public static interface Pipeline {
        
		void glBegin(int type);

		void onGlobalPipelineSet();

		void glVertex3f(float x, float y, float z);

		void glEnd();

		void glPolygonMode(int face, int mode);

		void glColor4f(float r, float g, float b, float a);

		void glFresnelTeamColor1f(float v);

		void glFresnelOpacity1f(float v);

		void glEmissiveGain1f(float renderEmissiveGain);

		void glFresnelColor3f(float r, float g, float b);

		void glNormal3f(float x, float y, float z);

		void glTexCoord2f(float u, float v);

		void glColor3f(float r, float g, float b);

		void glColor4ub(byte r, byte g, byte b, byte a);

		void glLight(int light, int pname, FloatBuffer params);

		void glRotatef(float angle, float axisX, float axisY, float axisZ);

		void glScalef(float x, float y, float z);

		void glTranslatef(float x, float y, float z);

		void glOrtho(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax);

		void gluPerspective(float fovY, float aspect, float nearClip, float farClip);

		void glLightModel(int lightModel, FloatBuffer ambientColor);

		void glMatrixMode(int mode);

		void glLoadIdentity();

		void glEnableIfNeeded(int glEnum);

		void glShadeModel(int glFlat);

		void glDisableIfNeeded(int glEnum);

		void prepareToBindTexture();

		void glTangent4f(float x, float y, float z, float w);

		void glActiveHDTexture(int textureUnit);

		void glViewport(int x, int y, int w, int h);

		void glCamera(ViewerCamera viewerCamera, boolean usingModelCamera);

		void discard();

		void setCurrentPipeline(int pipelineId);

		int getCurrentPipelineIndex();
	}
}
