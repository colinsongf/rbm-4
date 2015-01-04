# -*- coding: utf-8 -*-
"""
Created on Sat Jan  3 21:00:08 2015

@author: oleksiyp
"""

import struct
import array
import itertools
import numpy as np

from os import listdir
from os.path import isfile, isdir, join

def search_ffts(path, suf = ''):
    for series in listdir(path):
        p1 = join(path, series)
        if not isdir(p1):
            continue
        for part in listdir(p1):
            p2 = join(p1, part)
            if not isdir(p2):
                continue
            for file in listdir(p2):
                p3 = join(p2, file)
                if not isfile(p3):
                    continue
                if not file.endswith('.fft' + suf):
                    continue
                yield p3


def sum_fft(filename, sum, sum2, count):
    def init(sample_len, header):
        for i in range(sample_len - len(sum)):
            sum.append(0)
            sum2.append(0)
            count.append(0)
            
    def accept_vector(values):
        for i, val in enumerate(values):
            sum[i] += val
            sum2[i] += val * val
            count[i] += 1
    
    read_fft(filename, init, accept_vector)
    

def read_fft(filename, init, accept_vector):
    file = open(filename, mode='rb')
    
    n_samples = struct.unpack('!i', file.read(4))[0]
    sample_period = struct.unpack('!i', file.read(4))[0]
    sample_size = struct.unpack('!h', file.read(2))[0]
    sample_kind = struct.unpack('!h', file.read(2))[0]
    
    header = (n_samples, sample_period, sample_size, sample_kind)
    
    sample_len = sample_size / 4
    fmt = '!' + ('f' * sample_len)
    
    init(sample_len, header)
    
    for sampl in range(n_samples):
        bin_values = file.read(sample_size)
        values = struct.unpack(fmt, bin_values)
        accept_vector(values)
    
    
    file.close()

def norm_fft(filename, mean, deviation):
    file = open(filename + 'n', mode='wb')

    fmt = ['']                
    
    def write_fmt_value(format, value):
        bin = struct.pack(format, value)
        file.write(bin)
    
    def init(sample_len, header):
        n_samples = header[0]
        sample_period = header[1]
        sample_size = header[2]
        sample_kind = header[3]
        
        write_fmt_value('!i', n_samples)
        write_fmt_value('!i', sample_period)
        write_fmt_value('!h', sample_size)
        write_fmt_value('!h', sample_kind)
        
        fmt[0] = '!' + ('f' * sample_len)
                    
    def accept_vector(values):
        np_values = np.array(values)
        normed_values = (np_values - mean) / deviation
        values = normed_values.tolist()
        bin_values = struct.pack(fmt[0], *values)
        file.write(bin_values)
    
    read_fft(filename, init, accept_vector)
    
    write_fmt_value('!h', 0)
    
    file.close()
    
    
sum = array.array('d')
sum2 = array.array('d')
count = array.array('i')

print 'Finding sum and square sum'
for file in search_ffts("/home/oleksiyp/deeplearn/fft"):
    sum_fft(file, sum, sum2, count)

s = np.array(sum)
s2 = np.array(sum2)
c = np.array(count)

mean = s / count
square_mean = s2 / count

mean_square = mean * mean
deviation_sqaure = square_mean - mean_square
deviation = sqrt(deviation_sqaure)

print 'Mean ', mean
print 'Deviation', deviation

print 'Normalization'
for file in search_ffts("/home/oleksiyp/deeplearn/fft"):
    norm_fft(file, mean, deviation)

print 'Done'
