package com.gruposcout21.birthdayreminder.discord.dto;

import java.util.List;

public class WebhookRequest {
    private List<Embed> embeds;

    public WebhookRequest() {
    }

    public WebhookRequest(List<Embed> embeds) {
        this.embeds = embeds;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((embeds == null) ? 0 : embeds.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WebhookRequest other = (WebhookRequest) obj;
        if (embeds == null) {
            if (other.embeds != null)
                return false;
        } else if (!embeds.equals(other.embeds))
            return false;
        return true;
    }

    
}
