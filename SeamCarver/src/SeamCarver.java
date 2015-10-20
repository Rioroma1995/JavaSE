import java.awt.Color;

public class SeamCarver {
	private Picture picture;
	private Picture picture1;
	private boolean choose=false;
	private int width;
	private int height;
	private int length;

	private int[] energy;
	private int[] distTo;
	private int[] edgeTo;
	private int[] res;

	public SeamCarver(Picture pic) {
		picture = pic;
		picture1 = pic;
		width = pic.width();
		height = pic.height();
		length = width * height;
	}

	public int energy(int x, int y) {
		if (x == 0 || x == width - 1 || y == 0 || y == height() - 1)
			return 195075; // 2552^2 + 2552^2 + 2552^2 = 195075.
		Color left_color = picture.get(x - 1, y);
		Color right_color = picture.get(x + 1, y);
		Color top_color = picture.get(x, y - 1);
		Color down_color = picture.get(x, y + 1);
		return (lineEnergy(left_color, right_color) + lineEnergy(top_color,
				down_color));
	}

	private int lineEnergy(Color horizont, Color vertical) {
		int r = Math.abs(horizont.getRed() - vertical.getRed());
		int g = Math.abs(horizont.getGreen() - vertical.getGreen());
		int b = Math.abs(horizont.getBlue() - vertical.getBlue());
		return (r * r + g * g + b * b);
	}

	public int[] findHorizontalSeam() {
		initilize(true);
		res = new int[width];
		int number = 0;
		int start = 0;
		for (int col = 0; col < width - 1; ++col) {
			for (int row = 0; row < height; ++row) {
				number = getNumber(col, row);
				relax(number, getNumber(col + 1, row));
				if (row - 1 >= 0) {
					relax(number, getNumber(col + 1, row - 1));
				}
				if (row + 1 < height) {
					relax(number, getNumber(col + 1, row + 1));
				}
			}
		}
		for (int row = 1, minimalElement = distTo[getNumber(width - 1, 0)]; row < height; ++row) {
			if (distTo[getNumber(width - 1, row)] < minimalElement) {
				minimalElement = distTo[getNumber(width - 1, row)];
				start = getNumber(width - 1, row);
			}
		}
		while (start >= 0) {
			// -------colum--------------row
			res[start % width] = start / width;
			start = edgeTo[start];
		}
		return res;
	}

	public int[] findVerticalSeam() {
		initilize(false);
		res = new int[height];
		int number;
		int start = 0;
		for (int row = 0; row < height - 1; ++row) {
			for (int col = 0; col < width; ++col) {
				number = getNumber(col, row);
				relax(number, getNumber(col, row + 1));
				if (col - 1 >= 0)
					relax(number, getNumber(col - 1, row + 1));
				if (col + 1 < width)
					relax(number, getNumber(col + 1, row + 1));
			}
		}
		for (int col = 1, minimalElement = distTo[getNumber(col, height - 1)]; col < width; ++col) {
			if (distTo[getNumber(col, height - 1)] < minimalElement) {
				minimalElement = distTo[getNumber(col, height - 1)];
				start = getNumber(col, height - 1);
			}
		}
		while (start >= 0) {
			// --------row-------------colum
			res[start / width] = start % width;
			start = edgeTo[start];
		}
		return res;
	}

	private void initilize(boolean flag) {
		int number = 0;
		distTo = new int[length];
		edgeTo = new int[length];
		energy = new int[length];

		for (int col = 0; col < width; ++col) {
			for (int row = 0; row < height; ++row) {
				number = getNumber(col, row);
				if (flag) // horizontal
					if (col != 0)
						distTo[number] = Integer.MAX_VALUE;
					else
						distTo[number] = 0;
				else  // vertical
					if (row == 0) 
						distTo[number] = 0;
					else
						distTo[number] = Integer.MAX_VALUE;
				energy[number] = energy(col, row);
				edgeTo[number] = Integer.MIN_VALUE;
			}
		}
	}

	public void addHorizontalSeam(int[] a) {
		Picture newPicture = new Picture(width, height+1);
		Picture newPicture1 = new Picture(width, height+1);
		
		int point = 0;
		Color chosenColor = null;
		if (choose ==false){
			chosenColor = new Color(0, 0, 0);
			choose=true;
		}
		else{
			chosenColor = new Color(255, 255, 255);
			choose=false;
		}

		for (int col = 0; col < width; ++col) {
			point = a[col];
			for (int row = 0; row < height; ++row) {
				if (row < point) {
					newPicture.set(col, row, picture.get(col, row));
					newPicture1.set(col, row, picture1.get(col, row));
				} else {
					newPicture.set(col, row+1, picture.get(col, row));
					newPicture1.set(col, row+1, picture1.get(col, row));
				}
				if (row == point) {
					newPicture.set(col, row, chosenColor);
					newPicture1.set(col, row, findColor(col, row));
				}
			}
		}
		picture1 = newPicture1;
		reset(newPicture);
	}

	public void removeHorizontalSeam(int[] a) {
		Picture newPicture = new Picture(width, height - 1);
		int point = 0;
		for (int col = 0; col < width; ++col) {
			point = a[col];
			for (int row = 0; row < height - 1; ++row) {
				if (row < point)
					newPicture.set(col, row, picture.get(col, row));
				else
					newPicture.set(col, row, picture.get(col, row + 1));
			}
		}
		reset(newPicture);
	}

	public void addVerticalSeam(int[] a) {
		Picture newPicture = new Picture(width + 1, height);
		Picture newPicture1 = new Picture(width + 1, height);
		
		int point = 0;
		Color chosenColor = null;
		if (choose ==false){
			chosenColor = new Color(0, 0, 0);
			choose=true;
		}
		else{
			chosenColor = new Color(255, 255, 255);
			choose=false;
		}
		
		for (int row = 0; row < height; ++row) {
			point = a[row];
			for (int col = 0; col < width; ++col) {
				if (col < point) {
					newPicture.set(col, row, picture.get(col, row));
					newPicture1.set(col, row, picture1.get(col, row));
				} else {
					newPicture.set(col + 1, row, picture.get(col, row));
					newPicture1.set(col + 1, row, picture1.get(col, row));
				}
				if (col == point) {
					newPicture.set(col, row, chosenColor);
					newPicture1.set(col, row, findColor(col, row));
				}
			}
		}
		picture1 = newPicture1;
		reset(newPicture);
	}

	private Color findColor(int col, int row) {
		int R = 0;
		int G = 0;
		int B = 0;
		// крайня ліва верхня
		if (col == 0 && row == 0) {
			R = (picture1.get(0, 1).getRed() + picture1.get(1, 0).getRed()) / 2;
			G = (picture1.get(0, 1).getGreen() + picture1.get(1, 0).getGreen()) / 2;
			B = (picture1.get(0, 1).getBlue() + picture1.get(1, 0).getBlue()) / 2;
			return new Color(R, G, B);
		}
		// крайня ліва нижня
		if (col == 0 && row == height - 1) {
			R = (picture1.get(0, height - 2).getRed() + picture1.get(1,
					height - 1).getRed()) / 2;
			G = (picture1.get(0, height - 2).getGreen() + picture1.get(1,
					height - 1).getGreen()) / 2;
			B = (picture1.get(0, height - 2).getBlue() + picture1.get(1,
					height - 1).getBlue()) / 2;
			return new Color(R, G, B);
		}
		// крайня права верхня
		if (col == width - 1 && row == 0) {
			R = (picture1.get(width - 1, 1).getRed() + picture1.get(width - 2,
					0).getRed()) / 2;
			G = (picture1.get(width - 1, 1).getGreen() + picture1.get(
					width - 2, 0).getGreen()) / 2;
			B = (picture1.get(width - 1, 1).getBlue() + picture1.get(width - 2,
					0).getBlue()) / 2;
			return new Color(R, G, B);
		}
		// крайня права нижня
		if (col == width - 1 && row == height - 1) {
			R = (picture1.get(width - 1, height - 2).getRed() + picture1.get(
					width - 2, height - 1).getRed()) / 2;
			G = (picture1.get(width - 1, height - 2).getGreen() + picture1.get(
					width - 2, height - 1).getGreen()) / 2;
			B = (picture1.get(width - 1, height - 2).getBlue() + picture1.get(
					width - 2, height - 1).getBlue()) / 2;
			return new Color(R, G, B);
		}

		if ((col == 0 || col == width - 1) && (row > 0 && row < height - 1)) {
			R = (picture1.get(col, row - 1).getRed() + picture1.get(col,
					row + 1).getRed()) / 2;
			G = (picture1.get(col, row - 1).getGreen() + picture1.get(col,
					row + 1).getGreen()) / 2;
			B = (picture1.get(col, row - 1).getBlue() + picture1.get(col,
					row + 1).getBlue()) / 2;
		} else if ((row == 0 || row == height - 1)
				&& (col > 0 && col < width - 1)) {
			R = (picture1.get(col - 1, row).getRed() + picture1.get(col + 1,
					row).getRed()) / 2;
			G = (picture1.get(col - 1, row).getGreen() + picture1.get(col + 1,
					row).getGreen()) / 2;
			B = (picture1.get(col - 1, row).getBlue() + picture1.get(col + 1,
					row).getBlue()) / 2;
		} else {
			R = (picture1.get(col - 1, row).getRed()
					+ picture1.get(col + 1, row).getRed()
					+ picture1.get(col, row - 1).getRed() + picture1.get(col,
					row + 1).getRed()) / 4;
			G = (picture1.get(col - 1, row).getGreen()
					+ picture1.get(col + 1, row).getGreen()
					+ picture1.get(col, row - 1).getGreen() + picture1.get(col,
					row + 1).getGreen()) / 4;
			B = (picture1.get(col - 1, row).getBlue()
					+ picture1.get(col + 1, row).getBlue()
					+ picture1.get(col, row - 1).getBlue() + picture1.get(col,
					row + 1).getBlue()) / 4;
		}
		return new Color(R, G, B);
	}

	public void removeVerticalSeam(int[] a) {
		if (width < 2)
			throw new java.lang.IllegalArgumentException();
		if (a.length != height)
			throw new java.lang.IllegalArgumentException();
		Picture newPicture = new Picture(width - 1, height);
		int point = 0;
		for (int row = 0; row < height; ++row) {
			if (a[row] < 0 || a[row] > width - 1)
				throw new java.lang.IndexOutOfBoundsException();
			point = a[row];
			for (int col = 0; col < width - 1; ++col) {
				if (col < point) 
					newPicture.set(col, row, picture.get(col, row));
				else
					newPicture.set(col, row, picture.get(col + 1, row));
			}
		}
		reset(newPicture);
	}

	private void relax(int from, int to) {
		if (distTo[to] > distTo[from] + energy[to]) {
			distTo[to] = distTo[from] + energy[to];
			edgeTo[to] = from;
		}
	}

	private int getNumber(int col, int row) {
		return width * row + col;
	}

	private void reset(Picture newPicture) {
		width = newPicture.width();
		height = newPicture.height();
		picture = newPicture;
		length = newPicture.width() * newPicture.height();
		distTo = null;
		edgeTo = null;
		energy = null;
		res = null;
	}

	public Picture picture() {
		return picture;
	}

	public Picture picture1() {
		return picture1;
	}

	public void setPic1()
	{
		picture1=picture;
	}
	
	public void setPic()
	{
		picture=picture1;
	}
	
	public int width() {
		return width;
	}

	public int height() {
		return height;
	}
}