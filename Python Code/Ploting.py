__author__ = 'Anup'
import numpy as np
import matplotlib.pyplot as plt

mu, sigma = 100, 15
x = mu + sigma * np.random.randn(30)
print x
x=[24.7792207,26.0326087,26.30275229,25.47191011,27.48113208,24.875]
# the histogram of the data
n, bins, patches = plt.hist(x, 30, normed=1, facecolor='g', alpha=0.75)


plt.xlabel('Smarts')
plt.ylabel('Probability')
plt.title('Histogram of IQ')
plt.text(60, .025, r'$\mu=100,\ \sigma=15$')
plt.axis([40, 160, 0, 0.03])
plt.grid(True)
plt.show()