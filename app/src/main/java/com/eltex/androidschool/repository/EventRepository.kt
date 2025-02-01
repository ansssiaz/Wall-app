package com.eltex.androidschool.repository
import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.threeten.bp.Instant

interface EventRepository {
    fun getEvents(): Single<List<Event>>
    fun like(id : Long): Single<Event>
    fun deleteLike(id: Long): Single<Event>
    fun participate(id : Long): Single<Event>
    fun deleteParticipation(id: Long): Single<Event>
    fun saveEvent(id: Long, content: String, datetime: Instant): Single<Event>
    fun delete(id: Long): Completable
}