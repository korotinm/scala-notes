package my.training.akkatyped.step2

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.Signal
import akka.actor.typed.PostStop
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import java.util.concurrent.TimeUnit
import DeviceActor._


object RespondTemperatureActor {
  def apply(): Behavior[RespondTemperature] =
    Behaviors.setup(context => new RespondTemperatureActor(context))
  
}
class RespondTemperatureActor(context: ActorContext[DeviceActor.RespondTemperature]) 
	extends AbstractBehavior[DeviceActor.RespondTemperature] {

  override def onMessage(msg: RespondTemperature): Behavior[RespondTemperature] = 
    msg match {
      case RespondTemperature(rId, t) =>
      context.log.info(s"RespondTemperature actor: requestId:$rId; temperature=$t")
      this
    }        
}

object TemperatureRecordedActor {
  def apply(): Behavior[TemperatureRecorded] =
    Behaviors.setup(context => new TemperatureRecordedActor(context))
}
class TemperatureRecordedActor(context: ActorContext[DeviceActor.TemperatureRecorded]) 
	extends AbstractBehavior[DeviceActor.TemperatureRecorded] {

  override def onMessage(msg: TemperatureRecorded): Behavior[TemperatureRecorded] = 
    msg match {
      case TemperatureRecorded(rId) =>
      context.log.info(s"TemperatureRecorded actor: requestId:$rId")
      this
    }        
}

object DeviceActor {

	def apply(groupId: String, deviceId: Int): Behavior[DeviceMessage] = {
		Behaviors.setup(context => new DeviceActor(context, groupId, deviceId))
	}

	sealed trait DeviceMessage
	final case class ReadTemperature(requestId: Int, replyTo: ActorRef[RespondTemperature]) extends DeviceMessage
	final case class RespondTemperature(requestId: Int, value: Option[Double])

	final case class RecordTemperature(requestId: Int, value: Double, replyTo: ActorRef[TemperatureRecorded]) extends DeviceMessage
	final case class TemperatureRecorded(requestId: Int)
}

class DeviceActor(context: ActorContext[DeviceActor.DeviceMessage], groupId: String, deviceId: Int) 
	extends AbstractBehavior[DeviceActor.DeviceMessage] {

	var lastTemperature: Option[Double] = None

	context.log.info("Device actor {}-{}", groupId, deviceId)

	override def onMessage(msg: DeviceMessage): Behavior[DeviceMessage] = 
		msg match {
			case RecordTemperature(rId, t, replyTo) =>
				lastTemperature = Some(t)
				replyTo ! TemperatureRecorded(rId)
				this
			case ReadTemperature(rId, replyTo) =>
				replyTo ! RespondTemperature(rId, lastTemperature)
				this
		}

	override def onSignal: PartialFunction[Signal, Behavior[DeviceMessage]] = {
		case PostStop =>
			context.log.info("Device actor {}-{}", groupId, deviceId)
			this
	}    
	
}

//todo: add actor for registrating devices

object MainDevice extends App {
  val actorSys = ActorSystem(DeviceActor("GR1", 1), "deviceActorSys")
	val respondActorSys  = ActorSystem(RespondTemperatureActor(), "respondTemperActorSys")
	val temperatureRecSys  = ActorSystem(TemperatureRecordedActor(), "temperatureRecSys")

	actorSys ! RecordTemperature(10, 55.7, temperatureRecSys)
	actorSys ! ReadTemperature(10, respondActorSys)

	TimeUnit.SECONDS.sleep(10)
	actorSys.terminate()
	respondActorSys.terminate()
	temperatureRecSys.terminate()
}