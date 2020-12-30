package nl.louisa.booking.shared.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Repository<T extends Entity> {
    private final List<T> entities ;
    private T nullValue = null;

    public Repository(){
        this(new ArrayList<>());
    }

    public Repository(List<T> entities) {
        this.entities = new ArrayList<>(entities);
    }

    public void create(T entity) {
        validatePrimaryKey(entity);
        entities.add(entity);
    }

    private void validatePrimaryKey(T entity) {
        if (exists(entity.getId())){
            throw new RepositoryException(format("entity '%s' already exists [id=%s]",
                    entity.getClass().getSimpleName(),
                    entity.getId()));
        }
    }

    private boolean exists(String id) {
        return findBy(id) != this.nullValue;
    }

    public List<T> findAll() {
        return entities;
    }

    public void upsert(T entity) {
        T existingEntity = findBy(entity.getId());
        delete(existingEntity);
        create(entity);
    }

    public T findBy(String id) {
        return entities.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(this.nullValue);
    }

    @SafeVarargs
    public final List<T> findWhere(Predicate<T>... criteria) {
        List<Predicate<T>> predicates = new ArrayList<>();

        if (criteria != null) {
            stream(criteria)
                    .filter(Objects::nonNull)
                    .forEach(predicates::add);
        }

        return entities.stream()
                    .filter(predicates.stream().reduce(p ->true, Predicate::and))
                    .collect(Collectors.toList());
    }

    @SafeVarargs
    public final long countWhere(Predicate<T>... criteria){
        return findWhere(criteria).size();
    }

    public void delete(T entity) {
        entities.remove(entity);
    }

    public void setNullValue(T nullEntity) {
        this.nullValue = nullEntity;
    }

    public T getNullValue() {
        return nullValue;
    }

}
