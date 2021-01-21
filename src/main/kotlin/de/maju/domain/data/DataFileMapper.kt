package de.maju.domain.data

import com.maju.utils.IConverter
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface DataFileMapper : IConverter<DataFile, DataFileDTO>
