
#include "pch.h"
#include <iostream>
#include "extmem-c/extmem.h"
#include "stdlib.h"
#include "time.h"
#include "vector"

#define SEARCH_A_NUM 40
#define SEARCH_C_NUM 60
#define R_SIZE 112
#define	S_SIZE 224
#define BUF_SIZE 8

using namespace std;

int* generateData(int size, int low, int high) {
	srand((unsigned)time(NULL));
	int* result = new int[size];
	for (int i = 0;i < size;i++) {
		result[i] = (rand() % (high - low + 1)) + low;
	}
	return result;
}

void readBlock(unsigned index,Buffer buf) {
	unsigned char* blk;
	blk = readBlockFromDisk(index, &buf);
	int* result = new int[56];
	memcpy(result, blk, 56);
	for (int i = 0;i < 7;i++) {
		if (result[i * 2] != 0) {
			printf("%d ", result[i * 2]);
			printf("%d\n", result[i * 2 + 1]);
		}
	}
	unsigned next;
	memcpy(&next, blk + 56, 4);
	printf("%u\n", next);
	freeBlockInBuffer(blk, &buf);
}

void loadDataIntoDisk(int* A, int* B, int* C, int* D, Buffer buf) {
	unsigned char* blk;
	blk = getNewBlockInBuffer(&buf);
	unsigned disk_start = 1;
	int count = 0;
	for (int i = 0;i < R_SIZE;i++) {
		memcpy(blk + 8 * count, A + i, 4);
		memcpy(blk + 8 * count + 4, B + i, 4);
		count++;
		if (count == 7) {
			disk_start++;
			memcpy(blk + 56, &disk_start, 4);
			writeBlockToDisk(blk, disk_start - 1, &buf);
			blk = getNewBlockInBuffer(&buf);
			count = 0;
		}
	}
	int t1 = disk_start - 1;
	blk = readBlockFromDisk(t1, &buf);
	unsigned end = 0;
	memcpy(blk + 56, &end, 4);
	writeBlockToDisk(blk, t1, &buf);

	for (int i = 0;i < S_SIZE;i++) {
		memcpy(blk + 8 * count, C + i, 4);
		memcpy(blk + 8 * count + 4, D + i, 4);
		count++;
		if (count == 7) {
			disk_start++;
			memcpy(blk + 56, &disk_start, 4);
			writeBlockToDisk(blk, disk_start - 1, &buf);
			blk = getNewBlockInBuffer(&buf);
			count = 0;
		}
	}
	int t2 = disk_start - 1;
	blk = readBlockFromDisk(t2, &buf);
	memcpy(blk + 56, &end, 4);
	writeBlockToDisk(blk, t2, &buf);
}

void leanerSearch(Buffer buf, unsigned start1, unsigned start2, unsigned disk_start) {
	unsigned char* blk;
	unsigned char* new_blk = getNewBlockInBuffer(&buf);
	int count = 0;
	unsigned index1 = start1;
	while (index1 != 0) {
		blk = readBlockFromDisk(index1, &buf);
		int* read_data = new int[56];
		memcpy(read_data, blk, 56);
		for (int j = 0;j < 7;j++) {
			if (read_data[j * 2] == SEARCH_A_NUM ) {
				memcpy(new_blk + count * 8, blk + j * 8, 8);
				count++;
				if (count == 7) {
					disk_start++;
					memcpy(new_blk + 56, &disk_start, 4);
					writeBlockToDisk(new_blk, disk_start - 1, &buf);
					new_blk = getNewBlockInBuffer(&buf);
					count = 0;
				}
			}
		}
		memcpy(&index1, blk + 56, 4);
		freeBlockInBuffer(blk, &buf);
	}
	
	unsigned index2 = start2;
	while (index2 != 0) {
		blk = readBlockFromDisk(index2, &buf);
		int* read_data = new int[56];
		memcpy(read_data, blk, 56);
		for (int j = 0;j < 7;j++) {
			if (read_data[j * 2] == SEARCH_C_NUM) {
				memcpy(new_blk + count * 8, blk + j * 8, 8);
				count++;
				if (count == 7) {
					disk_start++;
					memcpy(new_blk + 56, &disk_start, 4);
					writeBlockToDisk(new_blk, disk_start - 1, &buf);
					new_blk = getNewBlockInBuffer(&buf);
					count = 0;
				}
			}
		}
		memcpy(&index2, blk + 56, 4);
		freeBlockInBuffer(blk, &buf);
	}

	for (int i = count;i < 7;i++) {
		int empty = 0;
		memcpy(new_blk + i * 8, &empty, 4);
		memcpy(new_blk + i * 8 + 4, &empty, 4);
	}

	unsigned end = 0;
	memcpy(new_blk + 56, &end, 4);
	writeBlockToDisk(new_blk, disk_start, &buf);
}

void readContinuedBlock(unsigned start, Buffer buf) {
	unsigned char* blk;
	unsigned index = start;
	int* data = new int[14];
	while (index != 0) {
		blk = readBlockFromDisk(index, &buf);
		memcpy(&index, blk + 56, 4);
		memcpy(data, blk, 56);
		for (int i = 0;i < 7;i++) {
			if (data[i * 2] != 0) {
				printf("%d ", data[i * 2]);
				printf("%d\n", data[i * 2 + 1]);
			}
		} 
		freeBlockInBuffer(blk, &buf);
	}
}

unsigned char* sortABlock(unsigned char* blk) {
	int* data = new int[14];
	memcpy(data, blk, 56);
	int temp;
	for (int i = 0;i < 7;i++) {
		for (int j = i;j < 7;j++) {
			if (data[i * 2] > data[j * 2]) {
				temp = data[i * 2];
				data[i * 2] = data[j * 2];
				data[j * 2] = temp;
				temp = data[i * 2 + 1];
				data[i * 2 + 1] = data[j * 2 + 1];
				data[j * 2 + 1] = temp;
			}
		}
	}
	memcpy(blk, data, 56);
	return blk;
}


/*void mergeSort(unsigned start, Buffer buf, unsigned result_start, int road) {
	printf("    %d\n", (&buf)->numFreeBlk);
	unsigned char* blk;
	unsigned char* new_blk;
	unsigned char* sorted_blk;
	unsigned temp = start;
	unsigned end = 0;
	unsigned count = 0;
	vector<int> merge_index; //记录要归并的那些排号序的头
	vector<int> new_merge_index;
	while (temp != 0) {
		count++;
		blk = readBlockFromDisk(temp, &buf);
		memcpy(&temp, blk + 56, 4);
		new_blk = getNewBlockInBuffer(&buf);
		sorted_blk = sortABlock(blk);
		memcpy(new_blk, sorted_blk, 56);
		memcpy(new_blk + 56, &end, 4);
		writeBlockToDisk(new_blk, result_start, &buf);
		merge_index.push_back(result_start); 
		freeBlockInBuffer(blk, &buf);
		result_start++;
	}

	printf("    %d\n", (&buf)->numFreeBlk);

	vector<unsigned char*> merge_blk; //记录要归并的那些块
	vector<int*> merge_blk_value; //记录要归并的那些块的值
	vector<int> merge_blk_index;
	int count1 = 0; //记录已经向缓存中读入了几个块

	while (true) {
		for (int i = 0;i < merge_index.size();i++) {
			blk = readBlockFromDisk(merge_index[i], &buf);
			merge_blk.push_back(blk);
			int* temp = new int[14];
			memcpy(temp, blk, 56);
			merge_blk_value.push_back(temp);
			printf("%d\n", merge_blk_value[0][2]);
			merge_blk_index.push_back(0);
			count1++;
			if (count1 == road || i == merge_index.size() - 1) {
				count1 = 0;
				new_merge_index.push_back(result_start);
				new_blk = getNewBlockInBuffer(&buf);
				int write_count = 0;
				while (true) {
					int min_index = -1;
					int min = 100;
					for (int j = 0;j < merge_blk.size();j++) {
						if (merge_blk_index[j] != 8) {
							if (min > merge_blk_value[j][merge_blk_index[j]]) {
								min = merge_blk_value[j][merge_blk_index[j]];
								min_index = j;
							}
						}
					}
					if (min_index == -1) {  //写完了
						break;
					}
					memcpy(new_blk + write_count * 8, merge_blk[min_index], 8);
					write_count++;
					merge_blk_index[min_index] ++;
					if (write_count == 7) {
						result_start++;
						memcpy(new_blk + 56, &result_start, 4);
						writeBlockToDisk(new_blk, result_start - 1, &buf);
						new_blk = getNewBlockInBuffer(&buf);
						write_count = 0;
					}
					if (merge_blk_index[min_index] == 7) {
						unsigned next;
						memcpy(&next, merge_blk[min_index] + 56, 4);
						if (next != 0) {
							freeBlockInBuffer(merge_blk[min_index], &buf);
							merge_blk[min_index] = readBlockFromDisk(next, &buf);
							int* temp_inside = new int[14];
							memcpy(temp_inside, merge_blk[min_index], 56);
							merge_blk_value[min_index] = temp_inside;
							merge_blk_index[min_index] = 0;
						}
						else {
							merge_blk_index[min_index] = 8;
						}
					}

				}
				freeBlockInBuffer(new_blk, &buf);
				unsigned index_end = result_start - 1;
				new_blk = readBlockFromDisk(index_end, &buf);
				memcpy(new_blk + 56, &end, 4);
				writeBlockToDisk(new_blk, index_end, &buf);
			}
		}
		merge_index = new_merge_index;
		if (merge_index.size() == 1) {
			break;
		}
	}
}*/

void insideSort(int* data, int length) {
	int temp;
	for (int i = 0;i < 7 * length;i++) {
		for (int j = i;j < 7 * length;j++) {
			
			if (data[i * 2] > data[j * 2]) {
				temp = data[i * 2];
				data[i * 2] = data[j * 2];
				data[j * 2] = temp;
				temp = data[i * 2 + 1];
				data[i * 2 + 1] = data[j * 2 + 1];
				data[j * 2 + 1] = temp;
			}
		}
	}
}


void mergeSort(unsigned start, Buffer buf, unsigned result_start) {
	unsigned char* blk;
	unsigned char* new_blk;
	unsigned temp = start;
	int* data = new int[BUF_SIZE * 56];
	int count = 0;
	unsigned end = 0;
	vector<unsigned> index;
	vector<unsigned char*> merge_data;
	vector<int*> merge_data_value;
	vector<int> merge_data_index;
	while (true) {
		int count = 0;
		index.push_back(result_start);
		for (int i = 0;i < BUF_SIZE;i++) {
			blk = readBlockFromDisk(temp, &buf);
			memcpy(data + 14 * i, blk, 56);
			memcpy(&temp, blk + 56, 4);
			freeBlockInBuffer(blk, &buf);
			count++;
			if (temp == 0) {
				break;
			}
		}

		insideSort(data, count);

		for (int i = 0;i < count;i++) {
			blk = getNewBlockInBuffer(&buf);
			memcpy(blk, data + 14 * i, 56);
			result_start++;
			memcpy(blk + 56, &result_start, 4);
			writeBlockToDisk(blk, result_start - 1, &buf);
		}

		unsigned t = result_start - 1;
		blk = readBlockFromDisk(result_start - 1, &buf);
		memcpy(blk + 56, &end, 4);
		writeBlockToDisk(blk, t, &buf);

		if (temp == 0) {
			break;
		}
	}

	for (int i = 0;i < index.size();i++) {
		merge_data.push_back(readBlockFromDisk(index[i], &buf));
		merge_data_index.push_back(0);
	}

	int merge_count = 0;
	new_blk = getNewBlockInBuffer(&buf);
	unsigned next;

	while (true) {
		int min = 100;
		int min_index = -1;
		int dt;
		for (int i = 0;i < merge_data.size();i++) {
			if(merge_data_index[i]!=8){
				memcpy(&dt, merge_data[i] + 8 * merge_data_index[i], 4);
				if (min > dt) {
					min = dt;
					min_index = i;
				}
			}
		}
		if (min_index == -1) {
			break;
		}
		blk = merge_data[min_index];
		memcpy(new_blk + merge_count * 8, merge_data[min_index] + 8 * merge_data_index[min_index], 8);
		merge_data_index[min_index]++;
		merge_count++;
		if (merge_count == 7) {
			result_start++;
			memcpy(new_blk + 56, &result_start, 4);
			writeBlockToDisk(new_blk, result_start-1, &buf);
			new_blk = getNewBlockInBuffer(&buf);
			merge_count = 0;
		}
		if (merge_data_index[min_index] == 7) {
			memcpy(&next, merge_data[min_index] + 56, 4);
			if (next == 0) {
				freeBlockInBuffer(merge_data[min_index], &buf);
				merge_data_index[min_index] = 8;
			}
			else {
				freeBlockInBuffer(merge_data[min_index], &buf);
				merge_data[min_index] = readBlockFromDisk(next, &buf);
				merge_data_index[min_index] = 0;
			}
		}
	}
	blk = readBlockFromDisk(result_start - 1, &buf);
	memcpy(blk + 56, &end, 4);
	writeBlockToDisk(blk, result_start - 1, &buf);
}

unsigned findBinaryStart(unsigned start, unsigned end, int value, Buffer buf) {
	unsigned current = start + (end - start) / 2;
	unsigned char* blk;
	unsigned search_start;
	int head;
	int tail;
	while (true) {
		blk = readBlockFromDisk(current, &buf);
		memcpy(&head, blk, 4);
		memcpy(&tail, blk + 48, 4);
		
		if (head < value && tail > value) {
			search_start = current;
			break;
		}
		else if (head > value) {
			end = current;
		}
		else if (tail < value) {
			start = current;
		}
		else if (head == value) {
			int temp = current;
			unsigned char* temp_blk;
			int data_head;
			while (true) {
				temp = temp - 1;
				temp_blk = readBlockFromDisk(temp, &buf);
				memcpy(&data_head, temp_blk, 4);
				if (data_head != value) {
					search_start = temp;
					freeBlockInBuffer(blk, &buf);
					freeBlockInBuffer(temp_blk, &buf);
					break;
				}
				freeBlockInBuffer(temp_blk, &buf);
			}
		}
		else if (tail == value) {
			search_start = current;
			freeBlockInBuffer(blk, &buf);
			break;
		}
		if (end == start) {
			search_start = 0;
			freeBlockInBuffer(blk, &buf);
			break;
		}
		if ((start - end) % 2 == 0) {
			current = start + (end - start) / 2;
		}
		else {
			current = start + (end - start) / 2 + 1;
		}
		freeBlockInBuffer(blk, &buf);
	}
	return search_start;
}

void binarySearch(unsigned start1, unsigned end1, int value1, unsigned start2, unsigned end2, int value2,unsigned write_start, Buffer buf) {
	unsigned search_start1 = findBinaryStart(start1, end1, value1, buf);
	unsigned search_start2 = findBinaryStart(start2, end2, value2, buf);
	unsigned char* blk;
	unsigned char* new_blk;
	int* data = new int[14];
	unsigned next = search_start1;
	new_blk = getNewBlockInBuffer(&buf);

	int count = 0;
	
	while (next != 0) {
		blk = readBlockFromDisk(next, &buf);
		memcpy(data, blk, 56);
		for (int i = 0;i < 7;i++) {
			if (data[i*2] == value1) {
				memcpy(new_blk + count * 8, data + i * 2, 8);
				count++;
				if (count == 7) {
					write_start++;
					memcpy(new_blk + 56, &write_start, 4);
					writeBlockToDisk(new_blk, write_start-1, &buf);
					new_blk = getNewBlockInBuffer(&buf);
				}
			}
		}
		if (data[12] != value1) {
			freeBlockInBuffer(blk, &buf);
			break;
		}
		memcpy(&next, blk + 56, 4);
		freeBlockInBuffer(blk, &buf);
	}

	next = search_start2;
	while (next != 0) {
		blk = readBlockFromDisk(next, &buf);
		memcpy(data, blk, 56);
		for (int i = 0;i < 7;i++) {
			if (data[i * 2] == value2) {
				memcpy(new_blk + count * 8, data + i * 2, 8);
				count++;
				if (count == 7) {
					write_start++;
					memcpy(new_blk + 56, &write_start, 4);
					writeBlockToDisk(new_blk, write_start - 1, &buf);
					new_blk = getNewBlockInBuffer(&buf);
					count = 0;
				}
			}
		}
		if (data[12] != value2) {
			break;
		}
		memcpy(&next, blk + 56, 4);
		freeBlockInBuffer(blk, &buf);
	}

	int empty = 0;
	for (int i = count;i < 7;i++) {
		memcpy(new_blk + i * 8, &empty, 4);
		memcpy(new_blk + i * 8 + 4, &empty, 4);
	}

	unsigned end = 0;
	memcpy(new_blk + 56, &end, 4);
	writeBlockToDisk(new_blk, write_start, &buf);
}

void projection(unsigned start, unsigned write_start, Buffer buf) {
	unsigned char* blk;
	unsigned char* new_blk = getNewBlockInBuffer(&buf);
	unsigned next = start;
	int* data = new int[14];

	int count = 0;
	while (next != 0) {
		blk = readBlockFromDisk(next, &buf);
		memcpy(data, blk, 56);
		for (int i = 0;i < 7;i++) {
			count++;
			memcpy(new_blk + count * 4, data + i * 2, 4);
			if (count == 15) {
				write_start++;
				memcpy(new_blk + 60, &write_start, 4);
				writeBlockToDisk(new_blk, write_start - 1, &buf);
				new_blk = getNewBlockInBuffer(&buf);
				count = 0;
			}
		}
		memcpy(&next, blk + 56, 4);
		freeBlockInBuffer(blk, &buf);
	}
	freeBlockInBuffer(new_blk, &buf);
	
	unsigned end = 0;
	blk = readBlockFromDisk(write_start - 1, &buf);
	memcpy(blk + 60, &end, 4);
	int empty = 0;
	for (int i = count;i < 15;i++) {
		memcpy(blk + i * 4, &empty, 4);
	}
	freeBlockInBuffer(blk, &buf);
}

void nextLoopJoin(unsigned start1, unsigned start2, unsigned write_start, Buffer buf) {
	int* data1 = new int[14*6]; //转换后的R的数据
	int* data2 = new int[14]; //转换后的S的数据
	int next1 = start1;
	int next2 = start2;
	int record_start2 = start2;
	unsigned char* blk;
	unsigned char* new_blk = getNewBlockInBuffer(&buf);
	vector<unsigned char*> record;
	int count = 0;
	int write_count = 0;
	while (next1 != 0) {
		for (int i = 0;i < 6;i++) {
			blk = readBlockFromDisk(next1, &buf);
			memcpy(data1 + i * 14, blk, 56);
			memcpy(&next1, blk + 56, 4);
			count++;
			record.push_back(blk);
			if (next1 == 0) {
				break;
			}
		}
		while (next2 != 0) {
			blk = readBlockFromDisk(next2, &buf);
			memcpy(data2, blk, 56);
			for (int i = 0;i < 7 * count;i++) {
				for (int j = 0;j < 7;j++) {
					if (data1[i * 2] == data2[j * 2]) {
						memcpy(new_blk + 12 * write_count, data1 + i * 2, 8);
						memcpy(new_blk + 12 * write_count + 8, data2 + j * 2 + 1, 4);
						write_count++;
						if (write_count == 5) {
							write_start++;
							memcpy(new_blk + 60, &write_start, 4);
							writeBlockToDisk(new_blk, write_start - 1, &buf);
							new_blk = getNewBlockInBuffer(&buf);
							write_count = 0;
						}
					}
				}
			}
			memcpy(&next2, blk + 56, 4);
			freeBlockInBuffer(blk, &buf);
		}
		for (int i = 0;i < count;i++) {
			freeBlockInBuffer(record[i], &buf);
		}
		count = 0;
		next2 = record_start2;
	}

	int empty = 0;
	if (write_count == 0) {
		freeBlockInBuffer(new_blk, &buf);
		new_blk = readBlockFromDisk(write_start - 1, &buf);
		memcpy(new_blk + 60, &empty, 4);
		writeBlockToDisk(new_blk, write_start - 1, &buf);
	}
	else {
		memcpy(new_blk + 60, &empty, 4);
		for (int i = write_count;i < 5;i++) {
			memcpy(new_blk + 12 * i, &empty, 4);
			memcpy(new_blk + 12 * i + 4, &empty, 4);
			memcpy(new_blk + 12 * i + 8, &empty, 4);
		}
		writeBlockToDisk(new_blk, write_start, &buf);
	}
}

void readJoinResult(int start, Buffer buf) {
	unsigned char* blk;
	int* data = new int[15];
	while (start != 0) {
		blk = readBlockFromDisk(start, &buf);
		memcpy(data, blk, 60);
		for (int i = 0;i < 5;i++) {
			if (data[i * 3] != 0) {
				printf("%d ", data[i * 3]);
				printf("%d ", data[i * 3 + 1]);
				printf("%d\n", data[i * 3 + 2]);
			}
		}
		memcpy(&start, blk + 60, 4);
		freeBlockInBuffer(blk, &buf);
	}
}

void sortMergeJoin(unsigned start1, unsigned start2, unsigned write_start, Buffer buf) {
	int* data1 = new int[14 * 2]; //存放关系R中的相同数据
	int* data2 = new int[14 * 2]; //存放关系S中的相同数据

	unsigned char* blk1 = readBlockFromDisk(start1, &buf); //读取关系R的块
	unsigned char* blk2 = readBlockFromDisk(start2, &buf); //读取关系S的块
	unsigned char* new_blk = getNewBlockInBuffer(&buf);
	int count_write = 0;
	int count1 = 0; 
	int count2 = 0; //计算已经读到块里的哪个位置
	int count_index1 = 0;
	int count_index2 = 0;  //记录已经写到了那个位置
	int current1; 
	int current2;
	while (start1 != 0 && start2 != 0) {
		if (count1 == 7) {
			memcpy(&start1, blk1 + 56, 4);
			freeBlockInBuffer(blk1, &buf);
			if (start1 == 0) {
				break;
			}
			else {
				blk1 = readBlockFromDisk(start1, &buf);
				count1 = 0;
			}
		}
		memcpy(&current1, blk1 + count1 * 8, 4);
		memcpy(data1, blk1 + count1 * 8, 8);
		count1++;
		count_index1++;
		while (true) {
			if (count1 == 7) {
				memcpy(&start1, blk1 + 56, 4);
				freeBlockInBuffer(blk1, &buf);
				if (start1 == 0) {
					break;
				}
				else {
					blk1 = readBlockFromDisk(start1, &buf);
					count1 = 0;
				}
			}
			int temp = current1;
			memcpy(&current1, blk1 + count1 * 8, 4);
			if (temp == current1) {
				memcpy(data1 + count_index1 * 2, blk1 + count1 * 8, 8);
				count1++;
				count_index1++;
			}
			else {
				memcpy(&current1, &temp, 4);
				break;
			}
		}

		if (count2 == 7) {
			memcpy(&start2, blk2 + 56, 4);
			freeBlockInBuffer(blk2, &buf);
			if (start2 == 0) {
				break;
			}
			else {
				blk2 = readBlockFromDisk(start2, &buf);
				count2 = 0;
			}
		}
		memcpy(&current2, blk2 + count2 * 8, 4);
		count2++;
		while (current2 < current1) {
			if (count2 == 7) {
				memcpy(&start2, blk2 + 56, 4);
				freeBlockInBuffer(blk2, &buf);
				if (start2 == 0) {
					break;
				}
				else {
					blk2 = readBlockFromDisk(start2, &buf);
					count2 = 0;
				}
			}
			memcpy(&current2, blk2 + count2 * 8, 4);
			count2++;
		}
		
		count2--;
		while (current2 == current1) {
			memcpy(data2 + count_index2 * 2, blk2 + count2 * 8, 8);
			count2++;
			count_index2++;
			if (count2 == 7) {
				memcpy(&start2, blk2 + 56, 4);
				freeBlockInBuffer(blk2, &buf);
				if (start2 == 0) {
					break;
				}
				else {
					blk2 = readBlockFromDisk(start2, &buf);
					count2 = 0;
				}
			}
			memcpy(&current2, blk2 + count2 * 8, 4);
		}

		if (count_index2 != 0) {
			for (int i = 0;i < count_index1;i++) {
				for (int j = 0;j < count_index2;j++) {
					memcpy(new_blk + count_write * 12, data1 + i * 2, 4);
					memcpy(new_blk + count_write * 12 + 4, data1 + i * 2 + 1, 4);
					memcpy(new_blk + count_write * 12 + 8, data2 + j * 2 + 1, 4);
					count_write++;
					if (count_write == 5) {
						write_start++;
						memcpy(new_blk + 60, &write_start, 4);
						writeBlockToDisk(new_blk, write_start - 1, &buf);
						new_blk = getNewBlockInBuffer(&buf);
						count_write = 0;
					}
				}
			}
		}
		count_index1 = 0;
		count_index2 = 0;
	}
	unsigned empty = 0;
	if (count_write == 0) {
		int t = write_start - 1;
		freeBlockInBuffer(new_blk, &buf);
		new_blk = readBlockFromDisk(t, &buf);
		memcpy(new_blk + 60, &empty, 4);
		writeBlockToDisk(new_blk, t, &buf);
	}
	else {
		memcpy(new_blk + 60, &empty, 4);
		for (int i = count_write;i < 5;i++) {
			memcpy(new_blk + 12 * i, &empty, 4);
			memcpy(new_blk + 12 * i + 4, &empty, 4);
			memcpy(new_blk + 12 * i + 8, &empty, 4);
		}
		writeBlockToDisk(new_blk, write_start, &buf);
	}
}


//定义的hash函数为 data mod 8
void hashHandler(unsigned start1, unsigned start2, unsigned hash_start1, unsigned hash_start2, Buffer buf) {
	unsigned char* blk;
	unsigned char* write_blk;
	int* data = new int[14];
	int count_init = 1;
	int zero = 0;
	while (start1 != 0) {
		blk = readBlockFromDisk(start1, &buf);
		memcpy(data, blk, 56);
		memcpy(&start1, blk + 56, 4);
		int hash;
		for (int i = 0;i < 7;i++) {
			hash = (data[i * 2] % 8) * 10 + hash_start1;
			if ((write_blk = readBlockFromDisk(hash, &buf)) == NULL) {
				write_blk = getNewBlockInBuffer(&buf);
				memcpy(write_blk, data + i * 2, 8);
				memcpy(write_blk + 60, &count_init, 4);
				writeBlockToDisk(write_blk, hash, &buf);
			}
			else {
				int count;
				memcpy(&count, write_blk + 60, 4);
				while (count == 7) {
					freeBlockInBuffer(write_blk, &buf);
					hash++;
					write_blk = readBlockFromDisk(hash, &buf);
					memcpy(&count, write_blk + 60, 4);
				}
				memcpy(write_blk + 8 * count, data + i * 2, 8);
				count++;
				if (count == 7) {
					int temp = hash + 1;
					memcpy(write_blk + 56, &temp, 4);
					memcpy(write_blk + 60, &count, 4);
					writeBlockToDisk(write_blk, hash, &buf);
					write_blk = getNewBlockInBuffer(&buf);
					memcpy(write_blk + 60, &zero, 4);
					writeBlockToDisk(write_blk, temp, &buf);
				}
				else {
					memcpy(write_blk + 60, &count, 4);
					writeBlockToDisk(write_blk, hash, &buf);
				}
			}
		}
		freeBlockInBuffer(blk, &buf);
	}

	int count;
	for (int i = 0;i < 8;i++) {
		int temp = i * 10;
		write_blk = readBlockFromDisk(temp + hash_start1, &buf);
		memcpy(&count, write_blk + 60, 4);
		while (count == 7) {
			freeBlockInBuffer(write_blk, &buf);
			temp++;
			write_blk = readBlockFromDisk(temp + hash_start1, &buf);
			memcpy(&count, write_blk + 60, 4);
		}
		memcpy(write_blk + 56, &zero, 4);
		for (int j = count;j < 7;j++) {
			memcpy(write_blk + 8 * j, &zero, 4);
			memcpy(write_blk + 8 * j + 4, &zero, 4);
		}
		writeBlockToDisk(write_blk, temp + hash_start1, &buf);
	}

	while (start2 != 0) {
		blk = readBlockFromDisk(start2, &buf);
		memcpy(data, blk, 56);
		memcpy(&start2, blk + 56, 4);
		int hash;
		for (int i = 0;i < 7;i++) {
			hash = (data[i * 2] % 8) * 10 + hash_start2;
			if ((write_blk = readBlockFromDisk(hash, &buf)) == NULL) {
				write_blk = getNewBlockInBuffer(&buf);
				memcpy(write_blk, data + i * 2, 8);
				memcpy(write_blk + 60, &count_init, 4);
				writeBlockToDisk(write_blk, hash, &buf);
			}
			else {
				int count;
				memcpy(&count, write_blk + 60, 4);
				while (count == 7) {
					freeBlockInBuffer(write_blk, &buf);
					hash++;
					write_blk = readBlockFromDisk(hash, &buf);
					memcpy(&count, write_blk + 60, 4);
				}
				memcpy(write_blk + 8 * count, data + i * 2, 8);
				count++;
				if (count == 7) {
					int temp = hash + 1;
					memcpy(write_blk + 56, &temp, 4);
					memcpy(write_blk + 60, &count, 4);
					writeBlockToDisk(write_blk, hash, &buf);
					write_blk = getNewBlockInBuffer(&buf);
					memcpy(write_blk + 60, &zero, 4);
					writeBlockToDisk(write_blk, temp, &buf);
				}
				else {
					memcpy(write_blk + 60, &count, 4);
					writeBlockToDisk(write_blk, hash, &buf);
				}
			}
		}
		freeBlockInBuffer(blk, &buf);
	}
	for (int i = 0;i < 8;i++) {
		int temp = i * 10;
		write_blk = readBlockFromDisk(temp + hash_start2, &buf);
		memcpy(&count, write_blk + 60, 4);
		while (count == 7) {
			freeBlockInBuffer(write_blk, &buf);
			temp++;
			write_blk = readBlockFromDisk(temp + hash_start2, &buf);
			memcpy(&count, write_blk + 60, 4);
		}
		memcpy(write_blk + 56, &zero, 4);
		for (int j = count;j < 7;j++) {
			memcpy(write_blk + 8 * j, &zero, 4);
			memcpy(write_blk + 8 * j + 4, &zero, 4);
		}
		writeBlockToDisk(write_blk, temp + hash_start2, &buf);
	}

}

void joinHandler(unsigned start1, unsigned start2, unsigned write_start, Buffer buf) {
	unsigned char* blk1;
	unsigned char* blk2;
	int* data1 = new int[14];
	int* data2 = new int[14];
	int count = 0;
	unsigned char* new_blk = getNewBlockInBuffer(&buf);
	for (int i = 0;i < 8;i++) {
		int read_start1 = start1 + i * 10;
		while (read_start1 != 0) {
			blk1 = readBlockFromDisk(read_start1, &buf);
			memcpy(data1, blk1, 56);
			memcpy(&read_start1, blk1 + 56, 4);
			int read_start2 = start2 + i * 10;
			while (read_start2 != 0) {
				blk2 = readBlockFromDisk(read_start2, &buf);
				memcpy(data2, blk2, 56);
				memcpy(&read_start2, blk2 + 56, 4);
				for (int m = 0;m < 7;m++) {
					if (data1[m * 2] != 0) {
						for (int n = 0;n < 7;n++) {
							if (data1[m * 2] == data2[n * 2]) {
								memcpy(new_blk + count * 12, data1 + m * 2, 8);
								memcpy(new_blk + count * 12 + 8, data2 + m * 2 + 1, 4);
								count++;
								if (count == 5) {
									write_start++;
									memcpy(new_blk + 60, &write_start, 4);
									writeBlockToDisk(new_blk, write_start - 1, &buf);
									new_blk = getNewBlockInBuffer(&buf);
									count = 0;
								}
							}
						}
					}
				}
				memcpy(&read_start2, blk2 + 56, 4);
				freeBlockInBuffer(blk2, &buf);
			}
			memcpy(&read_start1, blk1 + 56, 4);
			freeBlockInBuffer(blk1, &buf);
		}
	}

	unsigned empty = 0;
	if (count == 0) {
		int t = write_start - 1;
		freeBlockInBuffer(new_blk, &buf);
		new_blk = readBlockFromDisk(t, &buf);
		memcpy(new_blk + 60, &empty, 4);
		writeBlockToDisk(new_blk, t, &buf);
	}
	else {
		memcpy(new_blk + 60, &empty, 4);
		for (int i = count;i < 5;i++) {
			memcpy(new_blk + 12 * i, &empty, 4);
			memcpy(new_blk + 12 * i + 4, &empty, 4);
			memcpy(new_blk + 12 * i + 8, &empty, 4);
		}
		writeBlockToDisk(new_blk, write_start, &buf);
	}
}



int main()
{
	Buffer buf;
	
	size_t block_size = 64;
	size_t buf_size = 520;

	if (!initBuffer(buf_size, block_size, &buf)) {
		perror("init failed!\n");
		return -1;
	}

	//mergeSort()

	/*int* A = generateData(R_SIZE, 1, 40);
	int* B = generateData(R_SIZE, 1, 1000);
	int* C = generateData(S_SIZE, 20, 60);
	int* D = generateData(S_SIZE, 1, 1000);*/


	//loadDataIntoDisk(A, B, C, D, buf);

	//leanerSearch(buf, 1, 17, 100);

	//readBlock(750, buf);

	//readContinuedBlock(1000, buf);

	//mergeSort(17, buf, 300);

	//binarySearch(216, 231, SEARCH_A_NUM, 332, 363, SEARCH_C_NUM, 400, buf);

	//projection(1, 500, buf);

	//nextLoopJoin(1, 17, 600, buf);
	
	readJoinResult(1000, buf);

	//sortMergeJoin(216, 332, 700, buf);

	//hashHandler(1, 17, 800, 900, buf);

	//joinHandler(800, 900, 1000, buf);
}




