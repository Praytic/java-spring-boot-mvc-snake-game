package com.vchernogorov.model.game;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Snake {

    private Set<SnakePart> parts;

    public Snake(Set<SnakePart> parts) {
        this.parts = parts;
    }

    public Set<SnakePart> getParts() {
        return parts;
    }

    public SnakePart getHead() {
        return parts.stream()
                .filter(part -> part.getPreviousPart() == null)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Can't find snake's head."));
    }

    public SnakePart getTail() {
        return parts.stream()
                .filter(part -> part.getNextPart() == null)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Can't find snake's tail."));
    }

    public List<SnakePart> getBody() {
        return parts.stream()
                .filter(part -> part.getPreviousPart() != null)
                .collect(Collectors.toList());
    }
}