package ru.mail.teamcity.avatar.supplier;

/**
 * This class is extension of {@link AvatarSupplier}
 * Instances of this class should return avatar url, depending on
 * user parameters, e.g. login, name or email.
 * <p/>
 * So far, these avatar suppliers can be used globally, so there is
 * no need each user configure avatar personally.
 * <p/>
 * User: Grigory Chernyshev
 * Date: 16.05.13 23:39
 */

public interface IndividualAvatarSupplier extends AvatarSupplier {
}
