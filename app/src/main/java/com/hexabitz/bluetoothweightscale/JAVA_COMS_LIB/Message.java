package com.hexabitz.bluetoothweightscale.JAVA_COMS_LIB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {

  //region Variables
  private byte H = 0x48,
      Z = 0x5A,
      Length = 0,
      Destination,
      Source,
      Options,
      LSC,     // Least significance code byte.
      MSC,     // Most significance code byte.
      // Message byte array
      // ...
      CRC;
  private List<Byte> AllMessageList = new ArrayList<>();
  private byte[] Payload, AllMessage, OrganizedBuffer;
  //endregion


  /// <summary>
  /// The Hexabitz Buffer class wiki: https://hexabitz.com/docs/code-overview/array-messaging/
  /// The general constructor, all the payload parameters [Par1, Par2,...] must be included in the correct order within the Message array.
  /// </summary>
  public Message(byte Destination, byte Source,  byte Options, int Code, byte[] Payload){
    this.Destination = Destination;
    this.Source = Source;
    this.Options = Options;
    LSC = (byte)(Code & 0xFF); // Get the MSC & LSC automatically from the code.
    MSC = (byte)(Code >> 8);
    this.Payload = Payload;

    AllMessageList.add(H);
    AllMessageList.add(Z);
    AllMessageList.add(Length);
    AllMessageList.add(Destination);
    AllMessageList.add(Source);
    AllMessageList.add(Options);
    AllMessageList.add(LSC);
    if(MSC != 0) // If the code is only one byte so the MSC
      AllMessageList.add(MSC);

    for (byte item:Payload) {
      AllMessageList.add(item);
    }

    Length = (byte)(AllMessageList.size() - 3); // Not including H & Z delimiters, the length byte itself and the CRC byte
    // so its 4 but we didn't add the CRC yet so its 3.
    AllMessageList.set(2, Length); // Replace it with the correct length value.
    CRC = GetCRC();
    AllMessageList.add(CRC);
    AllMessage = new byte[AllMessageList.size()];
    int i = 0;
    for (byte value : AllMessageList) {
      AllMessage[i] = value;
      i++;
    }
}

  // Return the Cyclic Redundancy Check for the buffer.
  private byte GetCRC()  {
    List<Byte> organizedMessageList = Organize(AllMessageList); // Here we are organizing the buffer to calculate the CRC for it.

    OrganizedBuffer = new byte[organizedMessageList.size()];
    int i = 0;
    for (byte value : organizedMessageList) {
      OrganizedBuffer[i] = value;
      i++;
    }

    byte CRC = (byte) com.hexabitz.bluetoothweightscale.JAVA_COMS_LIB.CRC32.calc2(OrganizedBuffer,0,OrganizedBuffer.length);
    return CRC;
  }

  // Get the whole buffer.
  public byte[] GetAll()
  {
    return AllMessage;
  }

  // Routin for ordering the buffer to calculate the CRC for it.
  private List<Byte> Organize(List<Byte> MessageList)
  {
    List<Byte> OrganizedBuffer = new ArrayList<>();

    int MultiplesOf4 = 0;

    List<Byte> temp = new ArrayList<>();
    for(Byte item : MessageList)
    {
      temp.add(item);
      if (temp.size() == 4)
      {
        MultiplesOf4++;
        Collections.reverse(temp);
        OrganizedBuffer.addAll(temp);
        temp.clear();
      }
    }
    temp.clear();
    if ((MessageList.size() - OrganizedBuffer.size()) != 0)
    {
      int startingItem = MultiplesOf4 * 4;
      for (int i = startingItem; i < MessageList.size(); i++)
      {
        temp.add(MessageList.get(i));
      }

      while (temp.size() < 4)
        temp.add((byte) 0);
      Collections.reverse(temp);

      OrganizedBuffer.addAll(temp);
    }
    return OrganizedBuffer;
  }

  // Algorithm used in the Hexabitz modules hardware to calculate the correct CRC32 but we are only using the first byte in our modules.
//  private byte CRC32B(byte[] Buffer)  // Change byte to int to get the whole CRC32.
//  {
//    byte L = (byte)Buffer.length;
//    byte I, J;
//    uint CRC, MSB;
//    CRC = 0xFFFFFFFF;
//    for (I = 0; I < L; I++)
//    {
//      CRC ^= (((uint)Buffer[I]) << 24);
//      for (J = 0; J < 8; J++)
//      {
//        MSB = CRC >> 31;
//        CRC <<= 1;
//        CRC ^= (0 - MSB) & 0x04C11DB7;
//      }
//    }
//    return (byte)CRC; // Remove (byte) to get get the full int.
//  }

}
