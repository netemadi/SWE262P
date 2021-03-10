import sys, string
import numpy as np

# Example input: "Hello  World!" 
characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='<U1')

# Normalize
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.upper(characters)
# Result: array([' ', 'h', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'w', 'o', 'r', 'l', 'd', ' ', ' '], dtype='<U1')

### Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)
# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Result: array([[ 0,  6],
#                [ 6,  7],
#                [ 7, 13],
#                [13, 14]], dtype=int64)
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)

# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'), 
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Result: array(['hello', 'world'], dtype='<U5')

# Next, let's remove stop words
stop_words = np.array(list(set(open('stop_words.txt').read().upper().split(','))))
ns_words = swords[~np.isin(swords, stop_words)]


leet_chars = "48<D3ƒ6#!9K1мИ0PQЯ57UVШ%¥2"
all_upper_chars = string.ascii_uppercase

one_to_one_mapping = str.maketrans(all_upper_chars,leet_chars)
leetTranslated = np.char.translate(ns_words, one_to_one_mapping)

#to do 2-grams
leetWordsRepeated = np.repeat(leetTranslated, 2)
leetWords = np.reshape(leetWordsRepeated[1:-1], (-1, 2))

## Finally, count the word occurrences
uniq, counts = np.unique(leetWords, axis=0, return_counts=True)
wf_sorted = sorted(zip(uniq, counts), key=lambda t: t[1], reverse=True)

for w, c in wf_sorted[:5]:
    print(w[0], w[1], '-', c)