package RBTinput;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class RBTinput {
	public static void main(String[] args) throws IOException {
		int total = 0;
		RedBlackTreeNode rbt = new RedBlackTreeNode();
		
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        while(true) {
            String line = br.readLine();
            if ((Integer.parseInt(line) == 0)) break;
            else if ((Integer.parseInt(line) > 0)) {
            RedBlackTreeNode A = new RedBlackTreeNode(Integer.parseInt(line));
    		rbt.insert(rbt.root, A);
    		rbt.insertCase1(A);
    		total = total + 1;
            }
            else {
            	if(rbt.searchNode(Math.abs(Integer.parseInt(line))) == null) {
            		System.out.print("not found ");
            		System.out.println(Math.abs(Integer.parseInt(line)));
            	}
            	else {
            		rbt.delete(Math.abs(Integer.parseInt(line)));
            		total = total - 1;
            	}
            }

        }
        br.close();
        System.out.print("inorder : ");
        rbt.inorder(rbt.root);
        rbt.BH(rbt.root);
        System.out.println();
        System.out.print("bh : ");
        System.out.println(rbt.bh);
        System.out.print("nb : ");
        System.out.println(rbt.nb);
        System.out.print("total : ");
        System.out.println(total);
        
	}
}

class RedBlackTreeNode {
	public RedBlackTreeNode root;
	public int value;
	public RedBlackTreeNode left;
	public RedBlackTreeNode right;
	public RedBlackTreeNode parent;
	public Color color;
	public int nb = 0;
	public int bh = 0;

	public RedBlackTreeNode(int newval) {
		value = newval;
		left = null;
		right = null;
		parent = null;
		color = Color.red;
	}

	public RedBlackTreeNode() {
		root = null;
	}

	public void insert(RedBlackTreeNode tree, RedBlackTreeNode n) {
		if (root == null) {
			root = n;
		} else if (n.value < tree.value) {
			if (tree.left == null) {
				tree.left = n;
				n.parent = tree;
			} else {
				insert(tree.left, n);
			}
		} else {
			if (tree.right == null) {
				tree.right = n;
				n.parent = tree;
			} else
				insert(tree.right, n);
		}
	}

	public void print(RedBlackTreeNode tree, int level) {
		if (tree.right != null)
			print(tree.right, level + 1);
		for (int i = 0; i < level; i++)
			System.out.print("    ");
		System.out.print(tree.value);
		if (tree.color == Color.BLACK) {
			System.out.println("B");
		} else {
			System.out.println("R");
		}
		if (tree.left != null)
			print(tree.left, level + 1);
	}
	
	public void BH(RedBlackTreeNode tree) {
		if (tree.right == null) {
			if (tree.left == null) {
				if (tree.color == Color.BLACK) {
					bh = bh + 1;
				}
				while (tree.parent != null) {
					tree = tree.parent;
					if (tree.color == Color.BLACK) {
						bh = bh + 1;
					}
				}
			}
			else
				BH(tree.left);
		}
		else
			BH(tree.right);
		
		
	}
	
	public void inorder(RedBlackTreeNode tree) {
		if (tree == null)
			return;
		else {
			if (tree.color == Color.BLACK) {
				nb = nb + 1;
			} 
			inorder(tree.left);
			System.out.print(" " + tree.value);
			inorder(tree.right);
		}
	}

	public RedBlackTreeNode grandParent() {
		if (parent != null)
			return parent.getParent();
		else {
			return null;
		}
	}

	public RedBlackTreeNode sibling() {
		if (getParent() != null) {
			if (this == getParent().getLeft())
				return getParent().getRight();
			else
				return getParent().getLeft();
		} else {
			return null;
		}
	}

	public RedBlackTreeNode uncle() {
		return parent.sibling();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public RedBlackTreeNode getLeft() {
		return left;
	}

	public void setLeft(RedBlackTreeNode left) {
		this.left = left;
	}

	public RedBlackTreeNode getRight() {
		return right;
	}

	public void setRight(RedBlackTreeNode right) {
		this.right = right;
	}

	public RedBlackTreeNode getParent() {
		return parent;
	}

	public void setParent(RedBlackTreeNode parent) {
		this.parent = parent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void insertCase1(RedBlackTreeNode node) {
		if (node.getParent() == null)
			node.setColor(Color.BLACK);
		else
			insertCase2(node);
	}

	public void insertCase2(RedBlackTreeNode node) {
		if (node.getParent().getColor() == Color.BLACK)
			return;
		else
			insertCase3(node);
	}

	public void insertCase3(RedBlackTreeNode node) {
		RedBlackTreeNode uncle = node.uncle();
		RedBlackTreeNode grandParent;

		if ((uncle != null) && (uncle.getColor() == Color.RED)) {
			node.getParent().setColor(Color.BLACK);
			uncle.setColor(Color.BLACK);
			grandParent = node.grandParent();
			grandParent.setColor(Color.RED);

			insertCase1(grandParent);
		} else {
			insertCase4(node);
		}
	}

	public void insertCase4(RedBlackTreeNode node) {
		RedBlackTreeNode grandParent = node.grandParent();

		if ((node == node.getParent().getRight()) && (node.getParent() == grandParent.getLeft())) {
			rotateLeft(node.getParent());
			node = node.getLeft();
		} else if ((node == node.getParent().getLeft()) && (node.getParent() == grandParent.getRight())) {
			rotateRight(node.getParent());
			node = node.getRight();
		}
		insertCase5(node);
	}

	public void rotateLeft(RedBlackTreeNode node) {
		RedBlackTreeNode child = node.getRight();
		RedBlackTreeNode parent = node.getParent();

		if (child.getLeft() != null)
			child.getLeft().setParent(node);

		node.setRight(child.getLeft());
		node.setParent(child);
		child.setLeft(node);
		child.setParent(parent);

		if (parent != null) {
			if (parent.getLeft() == node)
				parent.setLeft(child);
			else
				parent.setRight(child);
		}else
			root = child;
	}

	public void rotateRight(RedBlackTreeNode node) {
		RedBlackTreeNode child = node.getLeft();
		RedBlackTreeNode parent = node.getParent();

		if (child.getRight() != null)
			child.getRight().setParent(node);

		node.setLeft(child.getRight());
		node.setParent(child);
		child.setRight(node);
		child.setParent(parent);

		if (parent != null) {
			if (parent.getRight() == node)
				parent.setRight(child);
			else
				parent.setLeft(child);
		} else {
			root = child;
		}
	}

	public void insertCase5(RedBlackTreeNode node) {
		RedBlackTreeNode grandParent = node.grandParent();
		node.getParent().setColor(Color.BLACK);
		grandParent.setColor(Color.RED);
		if (node == node.getParent().getLeft()) {
			rotateRight(grandParent);
		} else {
			rotateLeft(grandParent);
		}
	}

	public RedBlackTreeNode searchNode(int value) {// value 에 따른 노드를 찾는다
		RedBlackTreeNode node = root;
		while (node != null) {
			if (node.getValue() == value) {
				return node;
			} else if (node.getValue() < value) {
				node = node.getRight();
			} else {
				node = node.getLeft();
			}
		}
		return node;
	}

	private RedBlackTreeNode leftMaximumNode(RedBlackTreeNode node) {// 왼쪽노드 중
																		// 오른쪽으로
																		// 계속
																		// 찾아서
																		// 가장 큰
																		// 노드 ,
																		// 중위순회시
																		// 바로전 값
		while (node.getRight() != null) {
			node = node.getRight();
		}
		return node;
	}

	private void replaceNode(RedBlackTreeNode oldNode, RedBlackTreeNode newNode) { // 교체
		if (oldNode.getParent() == null) {
			root = newNode;
			newNode.color = Color.BLACK;
		} else {
			if (oldNode == oldNode.getParent().getLeft())
				oldNode.getParent().setLeft(newNode);
			else
				oldNode.getParent().setRight(newNode);
		}
		if (newNode != null) {
			newNode.setParent(oldNode.getParent());
		}
	}

	public void delete(int value) { // 삭제
		RedBlackTreeNode node = searchNode(value);
		if (node == null)
			return; // 없음
		if (node.getLeft() != null && node.getRight() != null) {
			RedBlackTreeNode leftMaxNode = leftMaximumNode(node.getLeft());
			node.value = leftMaxNode.value;
			node = leftMaxNode;
		}

		RedBlackTreeNode child = (node.getRight() == null) ? node.getLeft() : node.getRight();

		if (node.getColor() == Color.BLACK) { // 노드가 블랙이라면
			if (child == null) {
				node.setColor(Color.BLACK);
			} else {
				node.setColor(child.getColor());
			}
			deleteCase1(node);
		}
		replaceNode(node, child);
	}

	public void deleteCase1(RedBlackTreeNode node) {
		if (node.getParent() != null)
			deleteCase2(node);
	}

	public void deleteCase2(RedBlackTreeNode node) {
		RedBlackTreeNode sibling = node.sibling();
		if (sibling.getColor() == Color.RED) { // 형제의 color가 빨간색이면
			node.getParent().setColor(Color.RED);
			sibling.setColor(Color.BLACK); // color를 바꿔주고
			if (node == node.getParent().getLeft()) {
				rotateLeft(node.getParent()); // 왼쪽 자식이면 왼쪽으로 돌려주고
			} else {
				rotateRight(node.getParent());// 오른쪽 자식이면 왼쪽으로 돌려주고
			}
		}
		deleteCase3(node);
	}

	public void deleteCase3(RedBlackTreeNode node) {
		RedBlackTreeNode sibling = node.sibling();
		if ((node.getParent().getColor() == Color.BLACK) && (sibling.getColor() == Color.BLACK)
				&& (sibling.getLeft() == null ? true : sibling.getLeft().getColor() == Color.BLACK)
				&& (sibling.getRight() == null ? true : sibling.getRight().getColor() == Color.BLACK)) {
			sibling.setColor(Color.RED);
			deleteCase1(node.getParent()); // 속성 5 위반, 다시 돌아간다
		} else
			deleteCase4(node);
	}

	public void deleteCase4(RedBlackTreeNode node) {
		RedBlackTreeNode sibling = node.sibling();
		if ((node.getParent().getColor() == Color.RED) && (sibling.getColor() == Color.BLACK)
				&& (sibling.getLeft() == null ? true : sibling.getLeft().getColor() == Color.BLACK)
				&& (sibling.getRight() == null ? true : sibling.getRight().getColor() == Color.BLACK)) {
			sibling.setColor(Color.RED);
			node.getParent().setColor(Color.BLACK);
		} else
			deleteCase5(node);
	}

	public void deleteCase5(RedBlackTreeNode node) {
		RedBlackTreeNode sibling = node.sibling();
		if (sibling.getColor() == Color.BLACK) {
			if ((node == node.getParent().getLeft())
					&& (sibling.getRight() == null ? true : sibling.getRight().getColor() == Color.BLACK)
					&& (sibling.getLeft() == null ? false : sibling.getLeft().getColor() == Color.RED)) {
				sibling.setColor(Color.RED);
				sibling.getLeft().setColor(Color.BLACK);
				rotateRight(sibling);
			} else if ((node == node.getParent().getRight())
					&& (sibling.getLeft() == null ? true : sibling.getLeft().getColor() == Color.BLACK)
					&& (sibling.getRight() == null ? false : sibling.getRight().getColor() == Color.RED)) {
				sibling.setColor(Color.RED);
				sibling.getRight().setColor(Color.BLACK);
				rotateLeft(sibling);
			}
		}
		deleteCase6(node);
	}

	public void deleteCase6(RedBlackTreeNode node) {
		RedBlackTreeNode sibling = node.sibling();
		sibling.setColor(node.getParent().getColor());
		node.getParent().setColor(Color.BLACK);

		if (node == node.getParent().getLeft()) {
			if (sibling.getRight() != null) {
				sibling.getRight().setColor(Color.BLACK);
			}
			rotateLeft(node.getParent());
		} else {
			if (sibling.getLeft() != null) {
				sibling.getLeft().setColor(Color.BLACK);
			}
			rotateRight(node.getParent());
		}
	}

}
