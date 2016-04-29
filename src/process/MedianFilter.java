package process;

import java.awt.image.*;
import java.util.Arrays;

public class MedianFilter{
    
    private int size; //size of the square filter
    
    public MedianFilter(int size) {
       this.size = size;
    }
    
    public int getFilterSize() {
        return size;
    }
    
    //sort the array, and return the median value
    public int getMedian(int[] neighbours) {
        
    	//sorting arraynya
        Arrays.sort(neighbours);
        

    	int asize = neighbours.length;
        
        //jika jumlah tetangga ganjil
        if (asize%2 == 1) {
        	
        	double temp = (double) asize;
        	
        	int hsl = (int) Math.ceil( temp / 2.0);
        	
        	return neighbours[hsl];
        
        }
        else {
        	int left = asize / 2;
        	int right = left + 1;
        	
            return ( neighbours[left] + neighbours[right] ) / 2;
        
        }
    }
    
    /*
     * method untuk mencari dan mengambil value pixel tetangga di sekitarnya
     */
    public int[] getNeighbours(BufferedImage image, int wPos, int hPos)
    {
        
    	int[] neighbours; //store the pixel values of position(w, h) and its neighbors
        int height = image.getHeight() - 1;
        int width = image.getWidth() - 1;
        
        int wMinPos, wMaxPos, hMinPos, hMaxPos; 
        
        //mencari posisi w tetangga yang paling kiri
        wMinPos = wPos - size/2;

        //mencari posisi w tetangga yang paling kanan
        wMaxPos = wPos + size/2;
        

        //mencari posisi h tetangga yang paling atas
        hMinPos = hPos - size/2;
        

        //mencari posisi h tetangga yang paling bawah
        hMaxPos = hPos + size/2;
        
        //kalau posisi paling kiri w tetangga melewati batas, jadikan 0 
        if (wMinPos < 0){
            wMinPos = 0;
        }
        //kalau posisi paling kanan w tetangga melewati batas
        if (wMaxPos > width){
            wMaxPos = width;
        }
        if (hMinPos < 0){
            hMinPos = 0;
        }
        if (hMaxPos > height){
            hMaxPos = height;
        }
        
        //hitung jumlah ukuran array yang dibutuhkan untuk 
        //menyimpan value pixel tetangga
        int arraySize = (wMaxPos-wMinPos+1)*(hMaxPos-hMinPos+1);
        
        neighbours = new int[arraySize];
        
        int k = 0;
        
        //ambil value pixel tetangga dan taruh di variable neighbours
        for (int i = wMinPos; i <= wMaxPos; i++)
            for (int j = hMinPos; j <= hMaxPos; j++){
                neighbours[k] = image.getRGB(i, j); //get pixel value
                k++;
            }
        
        return neighbours;
    }
    
    /**
     * method utama untuk memfilter gambar
     * 
     * @param BufferedImage srcImage
     * @return BufferedImage
     */
    public BufferedImage filter(BufferedImage srcImage) {
    	
    	//baca tinggi gambar
        int height = srcImage.getHeight();
        //baca lebar gambar
        int width = srcImage.getWidth();
        
        //bikin objek gambar dengan ukuran yang sama dengan gambar yg mau di filter
        //tapi ini gambar masih kosong, objek gambar ini yang bakal di isi sama
        //hasil filter gambar
    	BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	
    	//variabel buat nampung pixel tetangga
        int[] neighbourPixels;
        
        for (int h = 0; h < height; h++){
            
        	for (int w = 0; w < width; w++) {
            	
        		//ambil semua value pixel tetangga
                neighbourPixels = getNeighbours(srcImage, w, h);
               
                int[] red, green, blue;
                red = new int[neighbourPixels.length];
                green = new int[neighbourPixels.length];
                blue = new int[neighbourPixels.length];
                
                //ambil value red green dan blue dari semua pixel tetangga
                for (int i = 0; i < neighbourPixels.length; i++) {
                    red[i] = Pixel.getRed( neighbourPixels[i] );
                    green[i] = Pixel.getGreen( neighbourPixels[i] );
                    blue[i] = Pixel.getBlue( neighbourPixels[i] );
                }
                
                //ambil median dari tiap warna
                int R = getMedian(red);
                int G = getMedian(green);
                int B = getMedian(blue);
                
                //bikin pixel baru hasil dari median filter
                int spixel = Pixel.createRGB(R, G, B);
                
                //save ke objek gambar yang kosong 
                tempImage.setRGB(w, h, spixel);
            }
        	
        }
        
        return tempImage;
    }
    
}
