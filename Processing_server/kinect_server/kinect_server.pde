import kinect4WinSDK.Kinect;
import kinect4WinSDK.SkeletonData;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import processing.video.*;
TcpIpServer4 server;

//The array to avoid overlap each thread's random number;
int[] randArr = new int [10];

Kinect kinect;
ArrayList <SkeletonData> bodies;

int[] l_h_point = {-1, -1};
int[] r_h_point = {-1, -1};

int[] head_point = {-1, -1};

int[] l_f_point = {-1, -1};
int[] r_f_point = {-1, -1};

int cir_size = 60;
int pointed1 = 1;
long tStart_l_h = 0;
long tStart_r_h = 0;
long tStart_head = 0;
long tStart_l_f = 0;
long tStart_r_f = 0;

int on_l_h = -1;
int on_r_h = -1;
int on_head = -1;
int on_l_f = -1;
int on_r_f = -1;

int conquer_time = 3500;

static int a;

String pressed;
void setup()
{
  size(800, 600);
  a = 5;
  int i;
  for ( i = 0 ; i < 10 ; i ++ )
  {
    randArr[i] = -1;
  }
   server = new TcpIpServer4(5);
    server.start();
  background(0);
  kinect = new Kinect(this);
  smooth();
  bodies = new ArrayList<SkeletonData>();

}



void draw()
{
  background(0);
  //image(kinect.GetImage(), 320, 0, 320, 240);
  image(kinect.GetDepth(), 0, 0, 800, 600);
  //image(kinect.GetMask(), 0, 240, 320, 240);
  //for (int i=0; i<bodies.size (); i++) 
  //{
    
    //20141025 BUG FIX
    //Use For Only One Person.
    int i = bodies.size();
    if( i != 0)
    {
      drawSkeleton(bodies.get(i-1));
      drawPosition(bodies.get(i-1));
    }
  if(l_h_point[0] == -1)//Left hand 1
  {
    fill(255, 204, 0, 128);
    ellipse(150, 150, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(150, 150, cir_size, cir_size);
  }
  
  if(l_h_point[1] == -1) //Left hand 2
  {
    fill(255, 204, 0, 128);
    ellipse(630, 430, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(630, 430, cir_size, cir_size);
  }
  
  if(r_h_point[0] == -1)//Right hand 1
  {
    fill(0, 0, 255, 128);
    ellipse(630, 180, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(630, 180, cir_size, cir_size);
  }
  
  if(r_h_point[1] == -1) //Right hand 2
  {
    fill(0, 0, 255, 128);
    ellipse(180, 410, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(180, 410, cir_size, cir_size);
  }
  
  if(head_point[0] == -1)//Head 1
  {
    fill(255, 255, 255, 128);
    ellipse(250, 120, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(250, 120, cir_size, cir_size);
  }
  
  if(head_point[1] == -1) //Head 2
  {
    fill(255, 255, 255, 128);
    ellipse(550, 320, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(550, 320, cir_size, cir_size);
  }
  
  if(l_f_point[0] == -1)//Left Hand2/Feet 1
  {
    fill(255, 0,255, 128);
    ellipse(650, 300, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(650, 300, cir_size, cir_size);
  }
  
  if(l_f_point[1] == -1) //Left Hand2/Feet 2
  {
    fill(255, 0, 255, 128);
    ellipse(300, 410, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(300, 410, cir_size, cir_size);
  }
  
  if(r_f_point[0] == -1)//Right Hand2/Feet 1
  {
    fill(0, 255, 0, 128);
    ellipse(530, 420, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(530, 420, cir_size, cir_size);
  }
  
  if(r_f_point[1] == -1) //Right Hand2/Feet 2
  {
    fill(0, 255, 0, 128);
    ellipse(210, 310, cir_size, cir_size);
  }
  else
  {
    fill(255, 0, 0, 128);
    ellipse(210, 310, cir_size, cir_size);
  }
    
  if(l_h_point[1] == 1 && l_h_point[0] == 1)//Left hand's mission is completed
  {
    pressed = "a";
    synchronized(g) {
          g.notify();
          redraw();
        }
      l_h_point[1] = -1;
     l_h_point[0] = -1;
    r_h_point[1] = -1;
     r_h_point[0] = -1;
     head_point[1] = -1;
     head_point[0] = -1; 
    l_f_point[1] = -1;
     l_f_point[0] = -1;
    r_f_point[1] = -1;
     r_f_point[0] = -1;
  
  }
  
  if(r_h_point[1] == 1 && r_h_point[0] == 1)//Right hand's mission is completed
  {
    pressed = "b";
    synchronized(g) {
          g.notify();
          redraw();
        }
      l_h_point[1] = -1;
     l_h_point[0] = -1;
    r_h_point[1] = -1;
     r_h_point[0] = -1;
     head_point[1] = -1;
     head_point[0] = -1; 
    l_f_point[1] = -1;
     l_f_point[0] = -1;
    r_f_point[1] = -1;
     r_f_point[0] = -1;
    
  }
  if(head_point[1] == 1 && head_point[0] == 1)//Head's mission is completed
  {
    pressed = "c";
    synchronized(g) {
          g.notify();
          redraw();
        }
      l_h_point[1] = -1;
     l_h_point[0] = -1;
    r_h_point[1] = -1;
     r_h_point[0] = -1;
     head_point[1] = -1;
     head_point[0] = -1; 
    l_f_point[1] = -1;
     l_f_point[0] = -1;
    r_f_point[1] = -1;
     r_f_point[0] = -1; 
   
  }
  if(l_f_point[1] == 1 && l_f_point[0] == 1)//Left foot's mission is completed
  {
    pressed = "d";
    synchronized(g) {
          g.notify();
          redraw();
        }
      l_h_point[1] = -1;
     l_h_point[0] = -1;
    r_h_point[1] = -1;
     r_h_point[0] = -1;
     head_point[1] = -1;
     head_point[0] = -1; 
    l_f_point[1] = -1;
     l_f_point[0] = -1;
    r_f_point[1] = -1;
     r_f_point[0] = -1; 

    
  }
  
  if(r_f_point[1] == 1 && r_f_point[0] == 1)//Right foot's mission is completed
  {
    pressed = "e";
    synchronized(g) {
          g.notify();
          redraw();
        }
     l_h_point[1] = -1;
     l_h_point[0] = -1;
    r_h_point[1] = -1;
     r_h_point[0] = -1;
     head_point[1] = -1;
     head_point[0] = -1; 
    l_f_point[1] = -1;
     l_f_point[0] = -1;
    r_f_point[1] = -1;
     r_f_point[0] = -1;
     
  }
  
}

void drawPosition(SkeletonData _s) 
{
  noStroke();
  fill(0, 100, 255);
  String s1 = str(_s.dwTrackingID);
  text(s1, _s.position.x*width, _s.position.y*height);
}

void drawSkeleton(SkeletonData _s) 
{
  // Body
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HEAD, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER, 
  Kinect.NUI_SKELETON_POSITION_SPINE);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT, 
  Kinect.NUI_SKELETON_POSITION_SPINE);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_SPINE);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SPINE, 
  Kinect.NUI_SKELETON_POSITION_HIP_CENTER);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HIP_CENTER, 
  Kinect.NUI_SKELETON_POSITION_HIP_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HIP_CENTER, 
  Kinect.NUI_SKELETON_POSITION_HIP_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HIP_LEFT, 
  Kinect.NUI_SKELETON_POSITION_HIP_RIGHT);

  // Left Arm
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT, 
  Kinect.NUI_SKELETON_POSITION_ELBOW_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_ELBOW_LEFT, 
  Kinect.NUI_SKELETON_POSITION_WRIST_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_WRIST_LEFT, 
  Kinect.NUI_SKELETON_POSITION_HAND_LEFT);

  // Right Arm
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_ELBOW_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_ELBOW_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_WRIST_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_WRIST_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_HAND_RIGHT);
  
  // Left Leg
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HIP_LEFT, 
  Kinect.NUI_SKELETON_POSITION_KNEE_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_KNEE_LEFT, 
  Kinect.NUI_SKELETON_POSITION_ANKLE_LEFT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_ANKLE_LEFT, 
  Kinect.NUI_SKELETON_POSITION_FOOT_LEFT);

  // Right Leg
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_HIP_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_KNEE_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_KNEE_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_ANKLE_RIGHT);
  DrawBone(_s, 
  Kinect.NUI_SKELETON_POSITION_ANKLE_RIGHT, 
  Kinect.NUI_SKELETON_POSITION_FOOT_RIGHT);
}

void DrawBone(SkeletonData _s, int _j1, int _j2) 
{
  noFill();
  stroke(50, 50, 50);
  if (_s.skeletonPositionTrackingState[_j1] != Kinect.NUI_SKELETON_POSITION_NOT_TRACKED &&
    _s.skeletonPositionTrackingState[_j2] != Kinect.NUI_SKELETON_POSITION_NOT_TRACKED) {
    line(_s.skeletonPositions[_j1].x*width, 
    _s.skeletonPositions[_j1].y*height, 
    _s.skeletonPositions[_j2].x*width, 
    _s.skeletonPositions[_j2].y*height);
  }
  float x= _s.skeletonPositions[_j2].x*width;
  float y= _s.skeletonPositions[_j2].y*height;
  float head_x= _s.skeletonPositions[_j1].x*width;
  float head_y= _s.skeletonPositions[_j1].y*height;

  if(_j2 == Kinect.NUI_SKELETON_POSITION_HAND_LEFT)//check lefthand
  {
    if(x >= 120 && x <= 180 && y >= 120 && y <= 180)
    {
      
      if(on_l_h == -1)
      {
        tStart_l_h = System.currentTimeMillis();

        on_l_h = 1;

      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_l_h;

       if (tDelta > conquer_time)
      {
        l_h_point[0] *= -1;
        tStart_l_h = System.currentTimeMillis();

      }
      
    }
    else if(x >= 600 && x <= 660 && y >= 400 && y <= 460)
    {
      if(on_l_h == -1)
      {
        tStart_l_h = System.currentTimeMillis();
        on_l_h = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_l_h;
      if (tDelta > conquer_time)
      {
        l_h_point[1] *= -1;
        tStart_l_h = System.currentTimeMillis();
      }
      
    }
    else
    {
      on_l_h = -1; 
    }
  }
  
  if(_j2 == Kinect.NUI_SKELETON_POSITION_HAND_RIGHT)//check right hand
  {

    if(x >= 600 && x <= 660 && y >= 150 && y <= 210)
    {
      if(on_r_h == -1)
      {
        tStart_r_h = System.currentTimeMillis();
        on_r_h = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_r_h;
      if (tDelta > conquer_time)
      {
        r_h_point[0] *= -1;
        tStart_r_h = System.currentTimeMillis();
      }
      
    }
    else if(x >= 150 && x <= 210 && y >= 380 && y <= 440)
    {
      if(on_r_h == -1)
      {
        tStart_r_h = System.currentTimeMillis();
        on_r_h = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_r_h;
      if (tDelta > conquer_time)
      {
        r_h_point[1] *= -1;
        tStart_r_h = System.currentTimeMillis();
      }
      
    }
    else
    {
      on_r_h = -1; 
    }
  }
  
  if(_j1 == Kinect.NUI_SKELETON_POSITION_HEAD)//check head
  {
 
    if(head_x >= 220 && head_x <= 280 && head_y >= 90 && head_y <= 150)
    {
      if(on_head == -1)
      {
        tStart_head = System.currentTimeMillis();
        on_head = 1;  
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_head;
      if (tDelta > conquer_time)
      {
        head_point[0] *= -1;
        tStart_head = System.currentTimeMillis();
      }
      
    }
    else if(head_x >= 520 && head_x <= 580 && head_y >= 290 && head_y <= 350)
    {
      if(on_head == -1)
      {
        tStart_head = System.currentTimeMillis();
        on_head = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_head;
      if (tDelta > conquer_time)
      {
        head_point[1] *= -1;
        tStart_head = System.currentTimeMillis();
      }
      
    }
    else
    {
      on_head = -1; 
    }
  }
  
  if(_j2 == Kinect.NUI_SKELETON_POSITION_FOOT_LEFT)//check left hand2/feet
  {

    if(x >= 620 && x <= 680 && y >= 270 && y <= 330)
    {
      if(on_l_f == -1)
      {
        on_l_f = 1;
        tStart_l_f = System.currentTimeMillis();
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_l_f;
      if (tDelta > conquer_time)
      {
        l_f_point[0] *= -1;
        tStart_l_f = System.currentTimeMillis();
      }
      
    }
    else if(x >= 270 && x <= 330 && y >= 380 && y <= 440)
    {
      if(on_l_f == -1)
      {
        tStart_l_f = System.currentTimeMillis();
        on_l_f = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_l_f;
      if (tDelta > conquer_time)
      {
        l_f_point[1] *= -1;
       tStart_l_f = System.currentTimeMillis();
      }
      
    }
    else
    {
      on_l_f = -1;
    }
  }
  
  if(_j2 == Kinect.NUI_SKELETON_POSITION_FOOT_RIGHT)//check right hand2/feet
  {
    if(x >= 500 && x <= 560 && y >= 390 && y <= 450)
    {
      if(on_r_f == -1)
      {
        tStart_r_f = System.currentTimeMillis();
        on_r_f = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_r_f;
      if (tDelta > conquer_time)
      {
        r_f_point[0] *= -1;
        tStart_r_f = System.currentTimeMillis();
      }
      
    }
    else if(x >= 180 && x <= 240 && y >= 280 && y <= 340)
    {
      if(on_r_f == -1)
      {
        
         tStart_r_f = System.currentTimeMillis();
        on_r_f = 1;
      }
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart_r_f;
      if (tDelta > conquer_time)
      {
        r_f_point[1] *= -1;
         tStart_r_f = System.currentTimeMillis();
      }
      
    }
    else
    {
      on_r_f = -1; 
    }
  }
}

void appearEvent(SkeletonData _s) 
{
  if (_s.trackingState == Kinect.NUI_SKELETON_NOT_TRACKED) 
  {
    return;
  }
  synchronized(bodies) {
    bodies.add(_s);
  }
}

void disappearEvent(SkeletonData _s) 
{
  synchronized(bodies) {
    for (int i=bodies.size ()-1; i>=0; i--) 
    {
      if (_s.dwTrackingID == bodies.get(i).dwTrackingID) 
      {
        bodies.remove(i);
      }
    }
  }
}

void moveEvent(SkeletonData _b, SkeletonData _a) 
{
  if (_a.trackingState == Kinect.NUI_SKELETON_NOT_TRACKED) 
  {
    return;
  }
  synchronized(bodies) {
    for (int i=bodies.size ()-1; i>=0; i--) 
    {
      if (_b.dwTrackingID == bodies.get(i).dwTrackingID) 
      {
        bodies.get(i).copy(_a);
        break;
      }
    }
  }
}


class TcpIpServer4 extends Thread {
  ServerSocket serverSocket;
  Thread[] threadArr;
  
  public TcpIpServer4(int num) {
    try {
      serverSocket = new ServerSocket(7777);
      System.out.println(getTime()+"Server is Ready");

      threadArr = new Thread[num];
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public void start() {
    for(int i=0; i < threadArr.length; i++) {
      threadArr[i] = new Thread(this);
      threadArr[i].start();
    }
  }

  public void run() {
    while(true) {
      try {
        String time = getTime();
        System.out.println(time+"Ready to Server");
        String msg = time.substring(time.indexOf("-")+1);
        int thread_No = Integer.parseInt(msg);
        Socket socket = serverSocket.accept();
        System.out.println(socket.getInetAddress() + "send Connection Request.");

        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
       InputStream in = socket.getInputStream();
      DataInputStream dis = new DataInputStream(in);
      String message = dis.readUTF();
      
      //The Process which can avoid overlap each thread's random number
      int rand_no = (int)random(11,20);
      int i;
      while(true)
      {
        for(i = 0 ; i < 10 ; i ++)
        {
          if(rand_no == randArr[i])
          {
            break;
          }
        }
        if( i == 10)
        {
          randArr[thread_No] = rand_no;
          break;
        }
        else
        {
          rand_no = (int)random(11,20);
        }
      }

      println("Thread No."+msg+" : " + rand_no);
      dos.writeUTF(Integer.toString(rand_no));
      System.out.println(message);

          while(true)
          {
            
           synchronized(g) {
                try {
                    g.wait();

                }
                catch (Exception e) {
                }
            }
            if(pressed == "a" && rand_no%3 == 0)//Left Hand
            {
              break;
            }
            else if(pressed == "b" && (rand_no%3 == 1))//Right Hand
            {
              break;
            }
            else if(pressed == "c" && (rand_no%3 == 2))//Head
            {
              break;
            }
            else if(pressed == "d" && (rand_no%3 == 3))//Left Hand2 / Feet
            {
              break;
            }
            else if(pressed == "e" && (rand_no%3 == 4))//Right Hand2 / Feet
            {
              break;
            }
            else
            {
              continue;
            }
          }     
        dos.writeUTF("E");
        System.out.println(getTime()+"send data."+a);
        // Ω∫∆Æ∏≤∞˙ º“ƒœ¿ª ¥›æ∆¡ÿ¥Ÿ.
        randArr[thread_No] = -1;
        dos.close();
        dis.close();
        in.close();
        out.close();
        socket.close();
      } catch (SocketTimeoutException  e) {
             System.out.println("Server Close.");
        System.exit(0);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } // while  
    
  } // run

  // «ˆ¿ÁΩ√∞£¿ª πÆ¿⁄ø≠∑Œ π›»Ø«œ¥¬ «‘ºˆ
  String getTime() {
    String name = Thread.currentThread().getName();
    SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
    return f.format(new Date()) + name ;
  }
} // class
