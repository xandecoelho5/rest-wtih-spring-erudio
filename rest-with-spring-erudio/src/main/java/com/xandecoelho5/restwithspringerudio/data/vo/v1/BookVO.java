package com.xandecoelho5.restwithspringerudio.data.vo.v1;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

public class BookVO extends RepresentationModel<BookVO> {

    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookVO bookVO = (BookVO) o;

        if (!Objects.equals(id, bookVO.id)) return false;
        if (!Objects.equals(author, bookVO.author)) return false;
        if (!Objects.equals(launchDate, bookVO.launchDate)) return false;
        if (!Objects.equals(price, bookVO.price)) return false;
        return Objects.equals(title, bookVO.title);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (launchDate != null ? launchDate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
