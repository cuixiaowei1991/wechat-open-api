package com.cn.crypto;

/**
 * ����/����ģ��ӿ�
 * 
 * @author Hartwell
 * 
 */
public interface CryptoModule {

	/**
	 * @param srcBytes
	 *            ԭbyte��
	 * @return ���ܺ��byte��
	 * @throws Exception
	 */
	public byte[] decode(byte[] srcBytes) throws Exception;

	/**
	 * @param srcBytes
	 *            ԭbyte��
	 * @return ���ܺ��byte��
	 * @throws Exception
	 */
	public byte[] encode(byte[] srcBytes) throws Exception;

	/**
	 * @param src
	 *            �������ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public String decode(String src) throws Exception;

	/**
	 * @param src
	 *            ԭ�ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public String encode(String src) throws Exception;
}
