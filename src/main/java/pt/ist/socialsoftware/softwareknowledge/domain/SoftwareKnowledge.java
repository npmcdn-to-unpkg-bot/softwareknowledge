package pt.ist.socialsoftware.softwareknowledge.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pt.ist.socialsoftware.softwareknowledge.utils.exception.SKErrorType;
import pt.ist.socialsoftware.softwareknowledge.utils.exception.SKException;




@Entity
@Table(name = "softwareKnowledge")
public class SoftwareKnowledge {
	static private SoftwareKnowledge instance = null;

	static public SoftwareKnowledge getInstance() {
		if (instance == null) {
			instance = new SoftwareKnowledge();
		}
		return instance;
	}

	private SoftwareKnowledge() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany(mappedBy = "softwareKnowledge",targetEntity=Category.class)
	private Set<Category> categorySet = new HashSet<Category>();
	
	
	
	//private Set<Source> sourceSet = new HashSet<Source>();
	//private Set<RelatedSource> relatedSourceSet = new HashSet<RelatedSource>();
	//private Set<RelatedCategory> relatedCategorySet = new HashSet<RelatedCategory>();

	public void clean() {
		categorySet.clear();
		//sourceSet.clear();
		//relatedSourceSet.clear();

	}

	public void addCategory(Category category) {
		if (categorySet.stream()
				.filter(c -> c.getCatId() == category.getCatId() || c.getDTO().getFullName().equals(category.getDTO().getFullName())).findFirst()
				.isPresent()) {
			throw new SKException(SKErrorType.DUPLICATE_CATEGORY, category.getCatId() + ":" + category.getName());
		}

		categorySet.add(category);
	}

//	public void addSource(Source source) {
//		if (sourceSet.stream()
//				.filter(s -> s.getSourceId() == source.getSourceId() || s.getName().equals(source.getName()))
//				.findFirst().isPresent()) {
//			throw new SKException(SKErrorType.DUPLICATE_SOURCE, source.getSourceId() + ":" + source.getName());
//		}
//
//		sourceSet.add(source);
//
//	}

	public void addRelatedSource(Source s1, Source s2, SourceProperty p) {
		for (RelatedSource r : getRelatedSourceSet()) {
			if (r.getS1().getName().equals(s1.getName()) && r.getS2().getName().equals(s2.getName())
					&& r.getSourceProperty() == p) {
				throw new SKException(SKErrorType.DUPLICATE_RELATEDSOURCE, s1.getName() + "," + s1.getName() + "," + p);
			}
			RelatedSource rs = new RelatedSource(s1, s2, p);
			getRelatedSourceSet().add(rs);
		}
	}

	public void addRelatedCategory(Category c1, Category c2, CategoryProperty p) {
		for (RelatedCategory r : getRelatedCategorySet()) {
			if (r.getC1().getName().equals(c1.getName()) && r.getC2().getName().equals(c2.getName())
					&& r.getCategoryProperty() == p) {
				throw new SKException(SKErrorType.DUPLICATE_RELATEDCATEGORY,
						c1.getName() + "," + c1.getName() + "," + p);
			}
			RelatedCategory rc = new RelatedCategory(c1, c2, p);
			getRelatedCategorySet().add(rc);
		}
	}

	public Category getCategory(String name) {
		for (Category c : getCategorySet()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public Category getCategory(int catId) {
		for (Category c : getCategorySet()) {
			if (c.getCatId() == catId) {
				return c;
			}
		}
		return null;
	}

	/*
	 * public Category getSubCategory(String name){ for(Category c :
	 * getSubCategorySet()){ if(c.getName().equals(name)){ return c; } } return
	 * null; }
	 * 
	 * public Category getSubCategory(int catId){ for(Category c :
	 * getSubCategorySet()){ if(c.getCatId() == catId){ return c; } } return
	 * null; }
	 */

	public Source getSource(String name) {
		return getSourceSet().stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);

	}

	public Source getSource(int sourceId) {
		for (Source s : getSourceSet()) {
			if (s.getSourceId() == sourceId) {
				return s;
			}
		}
		return null;
	}

	public Set<RelatedCategory> getRelatedCategorySet() {
		return null;//relatedCategorySet;
	}

	public void setRelatedCategorySet(Set<RelatedCategory> relatedCategorySet) {
		//this.relatedCategorySet = relatedCategorySet;
	}

	public Set<RelatedSource> getRelatedSourceSet() {
		return null;//relatedSourceSet;
	}

	public void setRelatedSourceSet(Set<RelatedSource> relatedSourceSet) {
		//this.relatedSourceSet = relatedSourceSet;
	}

	public void setCategorySet(Set<Category> categorySet) {
		this.categorySet = categorySet;
	}

	public void setSourceSet(Set<Source> sourceSet) {
		//this.sourceSet = sourceSet;
	}

	public Set<Category> getCategorySet() {
		return categorySet;
	}

	public Set<Source> getSourceSet() {
		return null;//sourceSet;
	}

	public Set<Category> getSubCategorySet(int catId) {
		for (Category c : getCategorySet()) {
			if (c.getCatId() == catId) {
				return c.getSubCategorySet();
			}
		}
		return null;
	}

	public Set<Category> removeCategory(int catId) {
		for(Category c : getCategorySet()){
			if(c.getCatId() == catId){
				getCategorySet().remove(c);
				return getCategorySet();
			}
		}
		return null;
	}

	public Set<Source> removeSource(int sourceId) {
		for(Source s : getSourceSet()){
			if(s.getSourceId() == sourceId){
				getSourceSet().remove(s);
				return getSourceSet();
			}
		}
		return null;
	}

	public Category updateCategory(String name, int catId) {
		for(Category c : getCategorySet()){
			if(c.getCatId() == catId){
				c.setName(name);
				return c;
			}
		}
		return null;
	}

	public Source updateSource(String name, int sourceId) {
		for(Source s : getSourceSet()){
			if(s.getSourceId() == sourceId){
				s.setName(name);
				return s;
			}
		}
		return null;
	}



}
