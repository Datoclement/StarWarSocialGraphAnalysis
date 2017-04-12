from graph_tool.all import *

import matplotlib
import matplotlib.cm as cm
import urllib2


# the threshold that eliminate the unimportant nodes
thred = 10

# the threshold that decide whose name to be printed on the picture
thred2 = 60

# the character by whom the graph picture is centred
person = 'Anakin_Skywalker'

# the outputfilename
outputfilename = 'Anakin_Skywalker.png'

f = open('../characterscraper/characterTableFile.txt','r')

vlist = list()

while (True):

    line = f.readline().strip()

    if (line == ''):
        break

    if (line.find('Legends') >= 0 or line.find('User') >= 0):
        continue

    vlist.append(urllib2.unquote(line).decode('utf8'))

# print vlist[4]

f = open('../completesocialgraph/csg.txt','r')

csg = dict()

while (True):

    line = filter(None, f.readline().strip().split(' '))

    if (len(line) == 0):
        break

    csg[line[0]] = set()

    for i in range(len(line)-1) :

        line[i+1] = urllib2.unquote(line[i+1]).decode('utf8')

        if(line[i+1].find('Legends') >= 0 or line[i+1].find('User') >= 0):
            continue

        if(line[i+1] == line[0]):
            continue

        csg[line[0]].add(line[i+1])

    csg[line[0]] = list(csg[line[0]])

# print csg['Yoda']

g = Graph()

Vs = dict()

for i in range(len(vlist)):

    if (not (vlist[i] in csg)):
        continue

    if (len(csg[vlist[i]])>=thred):

        Vs[vlist[i]] = g.add_vertex()

Vsr = g.new_vertex_property('string',val = '')

for i in range(len(vlist)):

    if (not (vlist[i] in csg)):
        continue

    if (len(csg[vlist[i]])>=thred2):

        Vsr[Vs[vlist[i]]] = vlist[i]

# print 'vs finished'

Deg = g.new_vertex_property('float',val = 0)
Degc = g.new_vertex_property('float',val = 0)

for i in range(len(vlist)):

    if (not (vlist[i] in csg)):
        continue

    if (len(csg[vlist[i]])>=thred):

        for j in range(len(csg[vlist[i]])):

            if (not (csg[vlist[i]][j] in Vs)):
                continue

            g.add_edge(Vs[vlist[i]],Vs[csg[vlist[i]][j]])

        Deg[Vs[vlist[i]]] = float(len(csg[vlist[i]]))/5.0
        Degc[Vs[vlist[i]]] = 1-float(len(csg[vlist[i]]))/1.0
        # Degc[Vs[vlist[i]]] = cm.rainbow(float(len(csg[vlist[i]]))/100.0)

# print type(min_spanning_tree(g,root=Vs['Yoda']))

bfstree = g.new_edge_property('bool',val = False)

class Visitor(BFSVisitor):

    def __init__(self, tree):
        self.tree = tree

    def tree_edge(self, e):
        self.tree[e] = True


bfs_search(g,Vs[person],Visitor(bfstree))
g.set_edge_filter(bfstree)

graph_draw(g,\
        # random_layout(g),\
        # arf_layout(g),\
        # sfdp_layout(g),\
        radial_tree_layout(g,root = Vs[person]), \
        output_size=(2000, 2000),\
        vertex_size = Deg,\
        vertex_fill_color = Degc, \
        vertex_text = Vsr, vertex_font_size = 18, vertex_text_position = 1,\
        edge_pen_width = 0.2,\
        vcmap=matplotlib.cm.gist_heat_r, output=outputfilename)
