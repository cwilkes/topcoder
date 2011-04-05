package com.ladro.topcoder.crossword;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Words {

	private final Set<String> m_words = new TreeSet<String>();
	private final Node m_rootNode = new Node(null);
	private final Map<String, Set<String>> m_fourLetterLookup = new TreeMap<String, Set<String>>();

	public Words(String[] words, int maxSize) {
		for (String word : words) {
			if (word.length() < maxSize) {
				addWord(word);
			}
		}
	}

	public void removeWord(String word) {
		m_words.remove(word);

	}

	public void addWord(String word) {
		/**
		 * Node top = m_rootNode; for (char c : word.toCharArray()) { top =
		 * top.getNode(c); } top.endWord();
		 */

		m_words.add(word);
		for (int i = 0; i < word.length() - 4; i++) {
			final String phrase = word.substring(i, i + 4);			
			if (!m_fourLetterLookup.containsKey(phrase)) {
				m_fourLetterLookup.put(phrase, new TreeSet<String>());
			}
			m_fourLetterLookup.get(phrase).add(word);
			
		}
	}

	private static class Node {
		private final Node[] m_subNodes = new Node[26];
		private final Node m_parent;
		private boolean m_endWord;

		public boolean isEndWord() {
			return m_endWord;
		}

		private Iterator<Character> getBranches() {
			return new Iterator<Character>() {
				int pos = 0;
				{
					advance();
				}

				@Override
				public boolean hasNext() {
					// TODO Auto-generated method stub
					return false;
				}

				private void advance() {
					while (m_subNodes[pos] == null) {
						pos++;
						if (pos >= m_subNodes.length) {
							break;
						}
					}
				}

				@Override
				public Character next() {
					char ret = (char) ('a' + pos);
					advance();
					return ret;
				}

				@Override
				public void remove() {
					// TODO Auto-generated method stub

				}
			};
		}

		public Node(Node parent) {
			m_parent = parent;
		}

		public void endWord() {
			m_endWord = true;
		}

		public Node getNode(char c) {
			int pos = c - 'a';
			if (m_subNodes[pos] == null) {
				m_subNodes[pos] = new Node(this);
			}
			return m_subNodes[pos];
		}

	}
}
