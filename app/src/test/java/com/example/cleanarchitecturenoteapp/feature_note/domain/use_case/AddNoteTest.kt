package com.example.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.example.cleanarchitecturenoteapp.data.repository.FakeNoteRepository
import com.example.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.example.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var addNote: AddNote
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var getNotes: GetNotes

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        addNote = AddNote(fakeNoteRepository)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Add blank title, throws Exception`() = runBlocking {
        val note = Note("", "content", 12, 12, 1)
        addNote.invoke(note)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Add blank content, throws Exception`() = runBlocking {
        val note = Note("title", "", 12, 12, 1)
        addNote.invoke(note)
    }

    @Test
    fun `Add correct content, `() = runBlocking {
        val note = Note("title", "content", 12, 12, 1)
        addNote.invoke(note)
        getNotes = GetNotes(fakeNoteRepository)
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()
        assertThat(notes.size).isGreaterThan(0)
    }
}