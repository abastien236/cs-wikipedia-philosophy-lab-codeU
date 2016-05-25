package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();
	final static String check = "https://en.wikipedia.org/wiki/Philosophy";
	static ArrayList<String> list = new ArrayList<String>();
	static int count = 0;

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link 2.
	 * Ignoring external links, links to the current page, or red links 3.
	 * Stopping when reaching "Philosophy", a page with no links or a page that
	 * does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		boolean attempt = crawl("https://en.wikipedia.org/wiki/Java_(programming_language)");
		if(attempt) {
			System.out.println("Success");
			return ;
		}
		System.out.println("Failure");
	}

	private static boolean crawl(String target) throws IOException {
		String url = target;
		Elements paragraphs = wf.fetchWikipedia(url);

		for (Element paras : paragraphs) {

			Iterable<Node> iter = new WikiNodeIterable(paras);
			for (Node node : iter) {
				if (node instanceof Element
						&& ((Element) node).tagName().equals("a")
						&& ((Element) node).parent().tagName().equals("p")) {
					count++;
					list.add(((Element) node).absUrl("href"));
					System.out.println(((Element) node).absUrl("href"));
					if (((Element) node).absUrl("href").equals(check)) {
						System.out.println(list);
						return true;
					} else {
						crawl(((Element) node).absUrl("href"));
						break;
					}
				}
			}
			if (count != 0) {
				break;
			}
		}
		if (count == 0) {
			return false;
		}
		return true;
	}
}
