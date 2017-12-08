package com.xhystc.zhihu.test;

public class Main
{
	static public void main(String[] args){
		int sum=1;
		for(int i=1;i<9999;i++){
			sum = 63*i;
			if(sum%3==0 && sum%7==0 && sum%2==1 && sum%4==1 && sum%5==4 && sum%6==3 &&sum%8==1)
			{
				System.out.println(sum);
				break;
			}

		}

	}
}
