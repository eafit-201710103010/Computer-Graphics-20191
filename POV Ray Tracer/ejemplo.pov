#include "colors.inc"
#include "glass.inc"
#include "golds.inc"
#include "metals.inc"
#include "stones.inc"
#include "woods.inc"
#include "textures.inc"

plane {
y, -3
texture {T_Stone24} 
}
     
#declare mirror1=plane {        
  <-1,40,40>, 1                 
  texture {T_Silver_1C}
};      
   
sphere {
<-3,0,0>, 1
texture {Blood_Marble} 
finish {
ambient 0.5
diffuse 0.1
phong 0.1
phong_size 10
//reflection 0.25
}
}
  
      
sphere {
<0,0,0>, 1   
texture{ pigment{color Magenta}
               normal {bumps 0.5 scale 0.05}
             } 
finish {
ambient 0.2
diffuse 0.6
phong .75
phong_size 100
}
}
  
  union{

sphere {
<3,0,0>, 1
pigment { Blue }
finish {
ambient 0.2
diffuse 0.6
phong .75
phong_size 1000
}
}
translate<0,2*clock-1,0>
}


light_source {
<10, 10, -10>
color White
}

camera {
location <0, 0, -10>
look_at <0, 0, 0> 
}       

mirror1