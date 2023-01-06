import Image
img = Image.new( 'RGB', (300,300), "black") # create a 300x300 image
pixels = img.load() # create the pixel map
for i in range(img.size[0]): # for every pixel:
  for j in range(img.size[1]):
    pixels[i,j] = (i, j, 100) # set a made-up color (R, G, B)
print pixels[100,100]
img.show() 
